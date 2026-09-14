package benchmark;

import warehouse.Warehouse;
import benchmark.ErrMsgLog;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.time.format.DateTimeFormatter;


/**
 * Бенчмарк колекции
 * создаем ArayList и LinkedList заполняем их
 * замеряем время операций add/remove/set
 * логи под колекции в logs/
 */
public class CollectionBenchmark {

    private static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000};
    private static final Random RND = new Random();

    public static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    /**
     * Прогоняем ArrayList по всем размерам
      */
    public void runArrayList() {
        for (int size: SIZES) {
            runListBenchmark("ArrayList", new ArrayList<Warehouse>(), size);
        }
    }

    /**
     * Прогоняем LinkedList по всем размерам
     */
    public void runLinkedList() {
        for (int size: SIZES) {
            runListBenchmark("LinkedList", new LinkedList<Warehouse>(), size);
        }
    }


    /**
     * Заполняет 1 колекцию рандомными складами
     * замеряем время операций
     * @param typeName "имя/тип" склада
     * @param list ссылка
     * @param size размер колекции
     */
    private void runListBenchmark(String typeName, List<Warehouse> list, int size) {
        String logFile = "logs/" + typeName + "_" + size + ".log";

        try (ErrMsgLog logger = new ErrMsgLog(logFile)) {
            logger.log("Start program: " + LocalDateTime.now().format(DATE_FMT));

            logger.log(typeName);


            // ---------- ADD-----------------
            long addTotalTime = 0;
            for (int i = 0; i < size; i++) {
                try {
                    long start = System.nanoTime();
                    list.add(RandomWarehouseGenerator.generate());
                    long duration = System.nanoTime() - start;
                    addTotalTime += duration;
                    logger.log("add, ID = " + i + ", " + duration);
                } catch (Exception e) {
                    logger.addErrWithLog(e);
                }
            }
            logger.log("addTotalCount = " + size);
            logger.log("addTotalTime = " + addTotalTime);
            logger.log("addAvgTime = " + (addTotalTime / size));


            // ----------- REMOVE --------------
            int removeCount = Math.max(1, size / 10);
            long removeTotalTime = 0;
            for (int i = 0; i < removeCount; i++) {
                try {
                    int index = RND.nextInt(list.size());
                    long start = System.nanoTime();
                    list.remove(index);
                    long duration = System.nanoTime() - start;
                    removeTotalTime += duration;
                    logger.log("remove, ID = " + index + ", " + duration);
                } catch (Exception e) {
                    logger.addErrWithLog(e);
                }
            }
            logger.log("removeTotalCount = " + removeCount);
            logger.log("removeTotalTime = " + removeTotalTime);
            logger.log("removeAvgTime = " + (removeTotalTime / removeCount));

            logger.log("Finish program: " + LocalDateTime.now().format(DATE_FMT));

        } catch (IOException e) {
            System.err.println("Не удалось создать лог " + logFile + ": " + e.getMessage());
        }
    }


    // Точка входа
    public static void main(String[] args) {

        CollectionBenchmark benchmark = new CollectionBenchmark();
        benchmark.runArrayList();
        benchmark.runLinkedList();

        System.out.println("Готово. Логи в папке logs/");
    }

}
