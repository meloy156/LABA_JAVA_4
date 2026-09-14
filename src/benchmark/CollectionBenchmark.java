package benchmark;

import warehouse.Warehouse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * Бенчмарк колекции
 * создаем ArayList и LinkedList заполняем их
 * замеряем время операций add/remove/set
 * Без логов и исключений
 */
public class CollectionBenchmark {

    private static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000};
    private static final Random RND = new Random();

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
        System.out.println("=== running " + typeName + " size=" + size + " ===");

        // ---------- ADD-----------------
        long addTotalTime = 0;
        for (int i = 0; i < size; i++) {
            long  start = System.nanoTime();
            list.add(RandomWarehouseGenerator.generate());
            long duration = System.nanoTime() - start;
            addTotalTime += duration;
        }
        long addAvgTime = addTotalTime / size;

        System.out.println("addTotalCount = " + size);
        System.out.println("addTotalTime = " + addTotalTime);
        System.out.println("addAvgTime = " + addAvgTime);


        // ----------- REMOVE --------------
        int removeCount = Math.max(1, size / 10);
        long removeTotalTime = 0;
        for (int i = 0; i < removeCount; i++) {
            int index = RND.nextInt(list.size());
            long start = System.nanoTime();
            list.remove(index);
            long duration = System.nanoTime() - start;
            removeTotalTime += duration;
        }
        long removeAvgTime = removeTotalTime / removeCount;

        System.out.println("removeTotalCount = " + removeCount);
        System.out.println("removeTotalTime = " + removeTotalTime);
        System.out.println("removeAvgTime = " + removeAvgTime);


    }


    // Проверка в этом комите
    public static void main(String[] args) {
        CollectionBenchmark benchmark = new CollectionBenchmark();
        benchmark.runArrayList();
        benchmark.runLinkedList();
    }

}
