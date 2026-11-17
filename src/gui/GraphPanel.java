package gui;

import parselogs.LogFileResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import java.util.TreeSet;

public class GraphPanel extends JComponent {

    public enum Metric { ADD_AVG, REMOVE_AVG, ADD_TOTAL, REMOVE_TOTAL }

    private static final String[] TITLES = {
            "Среднее время добавления (нс)",
            "Среднее время удаления (нс)",
            "Общее время добавления (нс)",
            "Общее время удаления (нс)"
    };

    private static final Color AL = Color.RED;
    private static final Color LL = Color.BLUE;
    private static final int LEFT = 90, TOP = 60, RIGHT = 40, BOTTOM = 70;

    private List<LogFileResult> data;
    private Metric currentMetric = Metric.ADD_AVG;

    private boolean logScaleX = false;
    private boolean logScaleY = false;
    private double zoom = 1.0;

    private LogFileResult hoveredResult = null;
    private String hoveredType = null;

    private int cPlotX, cPlotY, cPlotW, cPlotH;
    private double cXMin = 1, cXMax = 10, cYMin = 0, cYMax = 1;

    public GraphPanel() {
        setBackground(Color.WHITE);
        setOpaque(true);

        addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) zoomIn(); else zoomOut();
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e)  { updateHover(e.getX(), e.getY()); }
            @Override public void mouseDragged(MouseEvent e){ updateHover(e.getX(), e.getY()); }
        });

        addMouseListener(new MouseAdapter() {
            @Override public void mouseExited(MouseEvent e) {
                hoveredResult = null; hoveredType = null; repaint();
            }
        });
    }

    // ================== Публичный API ==================

    public void setData(List<LogFileResult> data) {
        this.data = data;
        this.hoveredResult = null;
        this.hoveredType = null;
        repaint();
    }

    public void setMetric(Metric m) {
        if (m != null) {
            this.currentMetric = m;
            this.hoveredResult = null;
            this.hoveredType = null;
            repaint();
        }
    }

    public void setLogScaleX(boolean v) { logScaleX = v; repaint(); }
    public void setLogScaleY(boolean v) { logScaleY = v; repaint(); }

    public void zoomIn()    { zoom *= 1.25; repaint(); }
    public void zoomOut()   { zoom /= 1.25; if (zoom < 0.25) zoom = 0.25; repaint(); }
    public void zoomReset() { zoom = 1.0; repaint(); }

    public Metric  getMetric()   { return currentMetric; }
    public boolean isLogScaleX() { return logScaleX; }
    public boolean isLogScaleY() { return logScaleY; }
    public double  getZoom()     { return zoom; }
    public void setZoom(double z){ zoom = (z > 0) ? z : 1.0; repaint(); }

    // ==================== Отрисовка ====================

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        int px = LEFT, py = TOP;
        int pw = w - LEFT - RIGHT;
        int ph = h - TOP - BOTTOM;
        if (pw <= 0 || ph <= 0) return;

        cPlotX = px; cPlotY = py; cPlotW = pw; cPlotH = ph;

        // Заголовок
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(Color.BLACK);
        String title = TITLES[currentMetric.ordinal()]
                + (logScaleX ? "  [log X]" : "")
                + (logScaleY ? "  [log Y]" : "")
                + (zoom != 1.0 ? String.format("  [x%.2f]", zoom) : "");
        g2.drawString(title, (w - g2.getFontMetrics().stringWidth(title)) / 2, 30);

        if (data == null || data.isEmpty()) {
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.setColor(Color.GRAY);
            String msg = "Нет данных. Нажмите «Загрузить логи…»";
            g2.drawString(msg, (w - g2.getFontMetrics().stringWidth(msg)) / 2, h / 2);
            return;
        }

        TreeSet<Integer> sizes = new TreeSet<>();
        for (LogFileResult r : data) sizes.add(r.size);
        if (sizes.size() < 2) return;

        // Диапазоны данных
        double xMin = sizes.first(), xMax = sizes.last();
        double yMin = 0, yMax = 1;
        for (LogFileResult r : data) yMax = Math.max(yMax, value(r));

        if (logScaleX) xMin = Math.max(1, xMin);
        if (logScaleY) yMin = 1;

        // Зум
        double vxMin, vxMax, vyMin, vyMax;
        if (logScaleX) {
            double lc = (Math.log10(xMin) + Math.log10(xMax)) / 2;
            double lh = (Math.log10(xMax) - Math.log10(xMin)) / 2 / zoom;
            vxMin = Math.pow(10, lc - lh);
            vxMax = Math.pow(10, lc + lh);
        } else {
            double c = (xMin + xMax) / 2, hr = (xMax - xMin) / 2 / zoom;
            vxMin = c - hr; vxMax = c + hr;
        }
        if (logScaleY) {
            double lc = (Math.log10(yMin) + Math.log10(yMax)) / 2;
            double lh = (Math.log10(yMax) - Math.log10(yMin)) / 2 / zoom;
            vyMin = Math.pow(10, lc - lh); vyMax = Math.pow(10, lc + lh);
            if (vyMin < 1) vyMin = 1;
        } else {
            double c = (yMin + yMax) / 2, hr = (yMax - yMin) / 2 / zoom;
            vyMin = Math.max(0, c - hr); vyMax = c + hr;
        }
        cXMin = vxMin; cXMax = vxMax; cYMin = vyMin; cYMax = vyMax;

        // Оси
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawLine(px, py, px, py + ph);
        g2.drawLine(px, py + ph, px + pw, py + ph);

        // Подписи X
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();
        for (int s : sizes) {
            double sx = mapX(s, vxMin, vxMax, px, pw);
            if (sx < px - 30 || sx > px + pw + 30) continue;
            g2.drawLine((int) sx, py + ph, (int) sx, py + ph + 5);
            String txt = String.valueOf(s);
            g2.drawString(txt, (int) (sx - fm.stringWidth(txt) / 2.0), py + ph + 20);
        }

        // Подписи Y
        if (logScaleY) {
            int lo = (int) Math.floor(Math.log10(vyMin));
            int hi = (int) Math.ceil(Math.log10(vyMax));
            for (int e = lo; e <= hi; e++) {
                double v = Math.pow(10, e);
                if (v < vyMin * 0.999 || v > vyMax * 1.001) continue;
                double sy = mapY(v, vyMin, vyMax, py, ph);
                g2.drawLine(px - 5, (int) sy, px, (int) sy);
                String txt = fmt((long) v);
                g2.drawString(txt, px - fm.stringWidth(txt) - 8, (int) sy + 4);
            }
        } else {
            for (int t = 0; t <= 4; t++) {
                double v = vyMin + (vyMax - vyMin) * t / 4.0;
                double sy = mapY(v, vyMin, vyMax, py, ph);
                g2.drawLine(px - 5, (int) sy, px, (int) sy);
                String txt = fmt((long) v);
                g2.drawString(txt, px - fm.stringWidth(txt) - 8, (int) sy + 4);
            }
        }

        g2.drawString("Размер коллекции", px + pw / 2 - 55, py + ph + 50);
        g2.drawString("Время, нс", 15, py + ph / 2);

        // Серии
        drawSeries(g2, "ArrayList",  vxMin, vxMax, vyMin, vyMax, AL);
        drawSeries(g2, "LinkedList", vxMin, vxMax, vyMin, vyMax, LL);

        // Подсказка при наведении
        drawHover(g2);

        // Легенда
        g2.setColor(AL); g2.fillRect(w - 170, 45, 15, 15);
        g2.setColor(Color.BLACK); g2.drawString("ArrayList",  w - 150, 57);
        g2.setColor(LL); g2.fillRect(w - 170, 65, 15, 15);
        g2.setColor(Color.BLACK); g2.drawString("LinkedList", w - 150, 77);
    }

    private void drawSeries(Graphics2D g2, String type,
                            double vxMin, double vxMax, double vyMin, double vyMax,
                            Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2f));

        int prevX = Integer.MIN_VALUE, prevY = Integer.MIN_VALUE;
        TreeSet<Integer> sizes = new TreeSet<>();
        for (LogFileResult r : data)
            if (r.collectionType.getCollectionName().equals(type)) sizes.add(r.size);

        for (int sz : sizes) {
            LogFileResult r = find(type, sz);
            if (r == null) continue;
            int x = (int) mapX(sz, vxMin, vxMax, cPlotX, cPlotW);
            int y = (int) mapY(value(r), vyMin, vyMax, cPlotY, cPlotH);
            if (prevX != Integer.MIN_VALUE) g2.drawLine(prevX, prevY, x, y);
            g2.fillOval(x - 4, y - 4, 8, 8);
            prevX = x; prevY = y;
        }
    }

    private void drawHover(Graphics2D g2) {
        if (hoveredResult == null || hoveredType == null) return;
        double sx = mapX(hoveredResult.size, cXMin, cXMax, cPlotX, cPlotW);
        double sy = mapY(value(hoveredResult), cYMin, cYMax, cPlotY, cPlotH);

        g2.setColor(hoveredType.equals("ArrayList") ? AL : LL);
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval((int) sx - 7, (int) sy - 7, 14, 14);

        String line1 = hoveredType + ", size = " + hoveredResult.size;
        String line2 = TITLES[currentMetric.ordinal()] + ": " + fmt(value(hoveredResult));

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();
        int tw = Math.max(fm.stringWidth(line1), fm.stringWidth(line2)) + 16;
        int th = fm.getHeight() * 2 + 12;
        int tx = (int) sx + 12;
        int ty = (int) sy - th - 12;
        if (tx + tw > getWidth() - 5) tx = (int) sx - tw - 12;
        if (ty < 5) ty = (int) sy + 12;
        if (tx < 5) tx = 5;

        g2.setColor(new Color(255, 255, 220, 245));
        g2.fillRoundRect(tx, ty, tw, th, 8, 8);
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(tx, ty, tw, th, 8, 8);
        g2.setColor(Color.BLACK);
        g2.drawString(line1, tx + 8, ty + fm.getAscent() + 4);
        g2.drawString(line2, tx + 8, ty + fm.getAscent() + fm.getHeight() + 4);
    }

    private void updateHover(int mx, int my) {
        if (data == null || data.isEmpty() || cPlotW <= 0) {
            hoveredResult = null; hoveredType = null; return;
        }
        LogFileResult best = null;
        String bestType = null;
        double bestDist = 12;

        for (String type : new String[]{"ArrayList", "LinkedList"}) {
            for (LogFileResult r : data) {
                if (!r.collectionType.getCollectionName().equals(type)) continue;
                double sx = mapX(r.size, cXMin, cXMax, cPlotX, cPlotW);
                double sy = mapY(value(r), cYMin, cYMax, cPlotY, cPlotH);
                double d = Math.hypot(mx - sx, my - sy);
                if (d < bestDist) { bestDist = d; best = r; bestType = type; }
            }
        }
        boolean changed = (best != hoveredResult)
                || (bestType != null && !bestType.equals(hoveredType));
        hoveredResult = best;
        hoveredType = bestType;
        if (changed) repaint();
    }

    private double mapX(double x, double xMin, double xMax, int pX, int pW) {
        double t;
        if (logScaleX) {
            double l  = Math.log10(Math.max(x, 1e-9));
            double lm = Math.log10(Math.max(xMin, 1e-9));
            double lM = Math.log10(Math.max(xMax, 1e-9));
            t = (lM - lm < 1e-9) ? 0.5 : (l - lm) / (lM - lm);
        } else {
            t = (xMax - xMin < 1e-9) ? 0.5 : (x - xMin) / (xMax - xMin);
        }
        return pX + t * pW;
    }

    private double mapY(double y, double yMin, double yMax, int pY, int pH) {
        double t;
        if (logScaleY) {
            double l  = Math.log10(Math.max(y, 1e-9));
            double lm = Math.log10(Math.max(yMin, 1e-9));
            double lM = Math.log10(Math.max(yMax, 1e-9));
            t = (lM - lm < 1e-9) ? 0.5 : (l - lm) / (lM - lm);
        } else {
            t = (yMax - yMin < 1e-9) ? 0.5 : (y - yMin) / (yMax - yMin);
        }
        return pY + pH - t * pH;
    }

    private LogFileResult find(String type, int size) {
        for (LogFileResult r : data)
            if (r.collectionType.getCollectionName().equals(type) && r.size == size) return r;
        return null;
    }

    private long value(LogFileResult r) {
        switch (currentMetric) {
            case ADD_AVG:      return r.getAddAvg();
            case REMOVE_AVG:   return r.getRemoveAvg();
            case ADD_TOTAL:    return r.getAddTotal();
            case REMOVE_TOTAL: return r.getRemoveTotal();
        }
        return 0;
    }

    private String fmt(long v) {
        if (v >= 1_000_000) return (v / 1_000_000) + "M";
        if (v >= 1_000)     return (v / 1_000) + "K";
        return String.valueOf(v);
    }
}