package tests;

import cargo.Car;
import cargo.Cargo;
import cargo.FrozenFood;
import cargo.Liquid;
import log.Logger;
import storage.BDWarehouse;
import train.Train;
import wagon.LiquidTank;
import wagon.RefrigeratorWagon;
import warehouse.Warehouse;

import javax.swing.table.TableRowSorter;
import java.io.File;
import java.io.PrintStream;

/**
 * Класс реализующий запуск и работу автотестов для всей программы
 */
public class Autotests {

    private static int passed = 0;
    private static int failed = 0;

    /**
     * Запуск всех автотество
     * @param print куда выводить
     */
    public static void runAll(PrintStream print) {
        passed = 0;
        failed = 0;

        print.println("\n\n||||| Запуск автотестов |||||");
        Logger.info("||||| СТАРТ АВТОТЕСТОВ |||||");


        // тесты
        testTrainLoading(print);
        testBD(print);

        // вывод
        print.println("-------------------");
        print.println("ИТОГ: PASS = " + passed + ", FAIL = " + failed);
        Logger.info("Результаты автотеста: [PASS] = " + passed + ", [FAIL] = " + failed);
    }

    /**
     * Проверка, вывод, логирование 1 теста
     * @param testName имя теста
     * @param condition прошел не
     * @param print куда выводить
     */
    private static void check(String testName, boolean condition, PrintStream print) {
        if (condition) {
            ++passed;
            print.println("- [PASS] " + testName);
            Logger.info("- [PASS] " + testName);
        } else {
            ++failed;
            print.println("- [FAIL] " + testName);
            Logger.info("- [FAIL] " + testName);
        }
    }


    /**
     * Тест загрузки поезда со склада
     * @param print куда выводить данные
     */
    private static void testTrainLoading(PrintStream print) {

        // создаем пробные данные
        Warehouse wh = new Warehouse(1, "ТЕст склад");
        wh.addCargo(new FrozenFood(1, "Курица", 20.0));
        wh.addCargo(new Liquid(2, "Вода", 50.0));

        Train t = new Train(1, "Тест поезд");
        t.addWagonList(new LiquidTank(1));

        // грузим
        int loaded = t.loadFromWarehouse(wh);

        // проверка загрузки
        // должен только 1 так как только 1 вагон
        check("Загрзука поезда: загрузилось в вагоны", loaded == 1, print);
        check("Загрузка поезда: осталось на складе", wh.size() == 1, print);
    }


    /**
     * Проверка корректности БД
     * загрузка/ выгрузка
     * @param print куда выводить строки
     */
    private static void testBD(PrintStream print) {
        String testDB = "data/test_autotest_db.db";

        try {
            // склад
            Warehouse wh = new Warehouse(1, "Склад для теста базы");
            wh.addCargo(new Car(1, "Тест машины", 5));

            // в базу
            BDWarehouse bd = new BDWarehouse(wh, testDB);
            bd.saveAll();

            // читаем из базы
            Warehouse newwh = new Warehouse(2, "Новый склад");
            BDWarehouse newbd = new BDWarehouse(newwh, testDB);
            newbd.loadAll();

            // проверка что прочитали
            check("БД: чтение элементов ", newwh.size() == 1, print);

            // проверка на корректность груза после базы
            if (newbd.size() == 1) {
                Cargo c = newwh.getAllCargo().get(0);
                check("БД: восстановление типа ", c instanceof Car, print);
                check("БД: восстановление веса ", c.getWeight() == 5.0, print);
            }
        } catch (Exception e) {
            check("БД: Ошибка выполнения тестов: " + e.getMessage(), false, print);
        } finally {
            File f = new File(testDB);
            if (f.exists())
                f.delete();
        }
    }

}
