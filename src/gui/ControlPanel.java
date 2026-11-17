package gui;

import parselogs.LogFileResult;
import parselogs.LogParse;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class ControlPanel extends JPanel {

    private static final String SETTINGS_FILE = "view.properties";

    private final GraphPanel graphPanel = new GraphPanel();
    private final JLabel status = new JLabel("Данные не загружены");

    private final JRadioButton[] metricRbs = new JRadioButton[4];
    private final GraphPanel.Metric[] metrics = {
            GraphPanel.Metric.ADD_AVG,
            GraphPanel.Metric.REMOVE_AVG,
            GraphPanel.Metric.ADD_TOTAL,
            GraphPanel.Metric.REMOVE_TOTAL
    };
    private final JCheckBox logX = new JCheckBox("Лог. X");
    private final JCheckBox logY = new JCheckBox("Лог. Y");

    public ControlPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));

        // Метрика
        top.add(new JLabel("График:"));
        ButtonGroup bg = new ButtonGroup();
        String[] names = {"Среднее ADD", "Среднее REMOVE", "Общее ADD", "Общее REMOVE"};
        for (int i = 0; i < names.length; i++) {
            final GraphPanel.Metric m = metrics[i];
            JRadioButton rb = new JRadioButton(names[i], i == 0);
            rb.addActionListener(e -> graphPanel.setMetric(m));
            bg.add(rb);
            metricRbs[i] = rb;
            top.add(rb);
        }

        // Лог-оси
        logX.addActionListener(e -> graphPanel.setLogScaleX(logX.isSelected()));
        logY.addActionListener(e -> graphPanel.setLogScaleY(logY.isSelected()));
        top.add(logX);
        top.add(logY);

        // Зум
        JButton zIn = new JButton("+");
        zIn.setToolTipText("Приблизить (или колесо мыши вверх)");
        zIn.addActionListener(e -> graphPanel.zoomIn());
        top.add(zIn);

        JButton zOut = new JButton("−");
        zOut.setToolTipText("Отдалить (или колесо мыши вниз)");
        zOut.addActionListener(e -> graphPanel.zoomOut());
        top.add(zOut);

        JButton zReset = new JButton("Сброс");
        zReset.setToolTipText("Вернуть исходный масштаб");
        zReset.addActionListener(e -> graphPanel.zoomReset());
        top.add(zReset);

        // Загрузка
        JButton loadBtn = new JButton("Загрузить логи…");
        loadBtn.addActionListener(e -> loadLogs());
        top.add(loadBtn);

        // Сохранение настроек
        JButton saveBtn = new JButton("Сохранить настройки");
        saveBtn.setToolTipText("Сохранить метрику / лог-оси / зум в view.properties");
        saveBtn.addActionListener(e -> saveSettings());
        top.add(saveBtn);

        top.add(status);

        add(top, BorderLayout.NORTH);
        add(graphPanel, BorderLayout.CENTER);
    }

    private void loadLogs() {
        JFileChooser fc = new JFileChooser("logs");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        Path dir = fc.getSelectedFile().toPath();
        try {
            List<LogFileResult> data = LogParse.parseDirectory(dir);
            graphPanel.setData(data);
            status.setText("Загружено: " + data.size() + " файлов");
        } catch (IOException ex) {
            status.setText("Ошибка загрузки");
            JOptionPane.showMessageDialog(this,
                    "Не удалось спарсить логи:\n" + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveSettings() {
        Properties p = new Properties();
        p.setProperty("metric",    graphPanel.getMetric().name());
        p.setProperty("logScaleX", String.valueOf(graphPanel.isLogScaleX()));
        p.setProperty("logScaleY", String.valueOf(graphPanel.isLogScaleY()));
        p.setProperty("zoom",      String.valueOf(graphPanel.getZoom()));

        try (FileOutputStream out = new FileOutputStream(SETTINGS_FILE)) {
            p.store(out, "view settings");
            status.setText("Настройки сохранены в " + SETTINGS_FILE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Не удалось сохранить настройки:\n" + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Вызывать при старте приложения. Применяет сохранённые настройки, если файл существует. */
    public void loadSettingsIfExists() {
        File f = new File(SETTINGS_FILE);
        if (!f.exists()) return;

        Properties p = new Properties();
        try (InputStream in = new FileInputStream(f)) {
            p.load(in);
        } catch (IOException ex) {
            return;
        }

        String mStr = p.getProperty("metric");
        if (mStr != null) {
            try {
                GraphPanel.Metric m = GraphPanel.Metric.valueOf(mStr);
                graphPanel.setMetric(m);
                for (int i = 0; i < metrics.length; i++)
                    if (metrics[i] == m) metricRbs[i].setSelected(true);
            } catch (IllegalArgumentException ignored) {}
        }

        boolean lx = Boolean.parseBoolean(p.getProperty("logScaleX", "false"));
        boolean ly = Boolean.parseBoolean(p.getProperty("logScaleY", "false"));
        logX.setSelected(lx);
        logY.setSelected(ly);
        graphPanel.setLogScaleX(lx);
        graphPanel.setLogScaleY(ly);

        try {
            graphPanel.setZoom(Double.parseDouble(p.getProperty("zoom", "1.0")));
        } catch (NumberFormatException ignored) {}
    }
}