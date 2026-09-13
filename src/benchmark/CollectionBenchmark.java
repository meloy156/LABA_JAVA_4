package benchmark;

import warehouse.Warehouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Бенчмарк колекции
 * создаем ArayList и LinkedList заполняем их
 * Без логов и исключений
 */
public class CollectionBenchmark {

    private static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000};


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
     * @param typeName "имя/тип" склада
     * @param list ссылка
     * @param size размер колекции
     */
    private void runListBenchmark(String typeName, List<Warehouse> list, int size) {
        System.out.println("running " + typeName + " size=" + size);

        for (int i = 0; i < size; i++) {
            list.add(RandomWarehouseGenerator.generate());
        }

        System.out.println(" loaded " + list.size() + " warehouses");
    }


    // Проверка в этом комите
    public static void main(String[] args) {
        CollectionBenchmark benchmark = new CollectionBenchmark();
        benchmark.runArrayList();
        benchmark.runLinkedList();
    }

}
