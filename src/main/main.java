package main;

import config.ConfigLoader;
import log.Logger;
import storage.BDWarehouse;
import ui.Authorization;
import ui.Menu;
import warehouse.Warehouse;
import tests.Autotests;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        PrintStream print = System.out;
        Scanner scanner = new Scanner(System.in);

        try {


            // Загрузка конфига
            ConfigLoader config = new ConfigLoader("config.ini");
            Logger.setLogFile(config.getLogFile());
            Logger.info("\n\n\n\n||||| СТАРТ ПРОГРАММЫ |||||");


            // Авторизация
            if (!Authorization.login(config, scanner, print)) {
                return;
            }

            // Запуск тестов
            if (config.isAutotests()) {
                Autotests.runAll(print);
            }

            // БД
            Warehouse warehouse = new Warehouse(1, "Основной склад");
            BDWarehouse bd = new BDWarehouse(warehouse, config.getDbFile());
            bd.loadAll();

            // Запуск меню
            Menu menu = new Menu(bd, config.isRoot());
            boolean flagRunning = true;
            while (flagRunning) {
                flagRunning = menu.run();
            }

            Logger.info("||||| ЗАВЕРШЕНЕИЕ ПРОГРАММЫ |||||");
        } catch (Exception e) {
            print.println("ОШибка: " + e.getMessage());
            Logger.error("Критическая ошибка: " + e.getMessage());
        }
    }
}
