package demo;

import cargo.*;
import train.Train;
import wagon.*;
import warehouse.Warehouse;

public class Demo {
    public static void main(String[] args) {
        // 1. Создаём склад
        Warehouse warehouse = new Warehouse(1, "Склад №1");

        // 2. Забиваем склад грузами всех 5 типов,
        //    причём с избытком — больше, чем влезет в один состав
        for (int i = 1; i <= 5; i++) {
            warehouse.addCargo(new Container(100 + i, "Контейнер-" + i, 20.0));
        }
        for (int i = 1; i <= 5; i++) {
            warehouse.addCargo(new Liquid(200 + i, "Жидкость-" + i, 20.0));
        }
        for (int i = 1; i <= 5; i++) {
            warehouse.addCargo(new BulkCargo(300 + i, "Сыпучий-" + i, 20.0));
        }
        for (int i = 1; i <= 15; i++) {
            warehouse.addCargo(new Car(400 + i, "Автомобиль-" + i, 1));
        }
        for (int i = 1; i <= 5; i++) {
            warehouse.addCargo(new FrozenFood(500 + i, "Заморозка-" + i, 5.0));
        }

        // 3. Создаём состав из 5 вагонов разных типов
        Train train = new Train(1, "Товарный-1");
        train.addWagonList(new ContainerPlatform(1));   // 70 т
        train.addWagonList(new LiquidTank(2));          // 60 т
        train.addWagonList(new BulkWagon(3));           // 60 т
        train.addWagonList(new CarTransporter(4));      // 10 машин
        train.addWagonList(new RefrigeratorWagon(5));   // 15 т

        // 4. Состояние до загрузки
        System.out.println("=== ДО ЗАГРУЗКИ ===");
        System.out.println(warehouse);
        System.out.println(train);
        printTrainDetails(train);

        // 5. Загружаем
        int loaded = train.loadFromWarehouse(warehouse);

        // 6. Состояние после загрузки
        System.out.println("\n=== ПОСЛЕ ЗАГРУЗКИ ===");
        System.out.println("Загружено грузовых единиц: " + loaded);
        System.out.println(warehouse);
        System.out.println(train);
        printTrainDetails(train);

        // 7. Что осталось на складе
        System.out.println("\n=== ОСТАТКИ НА СКЛАДЕ ===");
        for (Cargo c : warehouse.getAllCargo()) {
            System.out.println("  " + c);
        }
    }

    private static void printTrainDetails(Train train) {
        System.out.println("Состав поезда:");
        for (Wagon wagon : train.getWagonList()) {
            System.out.println("  " + wagon);
            for (Cargo cargo : wagon.getLoadedCargo()) {
                System.out.println("    -> " + cargo);
            }
        }
    }
}