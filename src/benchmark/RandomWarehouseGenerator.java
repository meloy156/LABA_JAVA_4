package benchmark;

import cargo.*;
import warehouse.Warehouse;

import java.util.Random;

/**
 * Фабрика случайных складов для тестирования
 * только создает рандомный заполненный склад
 */
public class RandomWarehouseGenerator {
    private static final Random RND = new Random();


    private static final int MIN_CARGO_FOR_WAREHOUSE = 1;
    private static final int MAX_CARGO_FOR_WAREHOUSE = 10;

    private static final int ID_BOUND = 1_000_000;
    private static final double MIN_WEIGHT = 1.0;
    private static final double MAX_WEIGHT = 100.0;


    private RandomWarehouseGenerator() {}

    /**
     * Создаем склад с рандом именем/айди и 1...10 рандом грузов
     */
    public static Warehouse generate() {
        int id = RND.nextInt(ID_BOUND);
        Warehouse warehouse = new Warehouse(id, "Warehouse-" + id);

        int cargoCount = MIN_CARGO_FOR_WAREHOUSE
                + RND.nextInt(MAX_CARGO_FOR_WAREHOUSE
                    - MIN_CARGO_FOR_WAREHOUSE);

        for(int i = 0; i < cargoCount; ++i) {
            warehouse.addCargo(randomCargo());
        }

        return warehouse;
    }

    /**
     * Один случайный груз любого из 5 типа
     */
    private static Cargo randomCargo() {
        int id = RND.nextInt(ID_BOUND);
        String name = "Cargo" + id;

        double weight = MIN_WEIGHT + RND.nextDouble(MAX_WEIGHT);

        return switch (RND.nextInt(5)) {
            case 0 -> new Container(id, name, weight);
            case 1 -> new Liquid(id, name, weight);
            case 2 -> new BulkCargo(id, name, weight);
            case 3 -> new Car(id, name, weight);
            case 4 -> new FrozenFood(id, name, weight);
            default -> throw new IllegalStateException("Нет такого выбора");
        };
    }
}
