package ui;

import cargo.*;
import storage.BDWarehouse;

import java.util.Locale;
import java.util.Scanner;


public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final BDWarehouse bdWarehouse;
    private final boolean isRoot;

    public Menu(BDWarehouse bdWarehouse, boolean isRoot) {
        this.bdWarehouse = bdWarehouse;
        this.isRoot = isRoot;
    }


    public boolean run() {
        System.out.println("||||| Главное меню ||||||");
        System.out.println("1. Показать все грузы");
        System.out.println("2. Добавить новый груз");
        System.out.println("3. Удалить груз по ID");
        System.out.println("0. Выход");

        if (isRoot) {
            System.out.println("---- Для администратора ----");
            System.out.println("7. Откладка");
            System.out.println("8. Запуск автотестов");
        }

        System.out.println("Выберите действие");
        String choise = scanner.nextLine().trim();


        switch (choise) {
            case "1" -> showAll();
            case "2" -> addCargo();
            case "3" -> deleteCargo();
            case "7" -> {
                if (isRoot)
                    System.out.println("[ОТКЛАДКА] Сейчас в базе: ");
                else System.out.println("Доступ запрещен");
            }
            case "8" -> {
                if (isRoot)
                    System.out.println("[Автотесты] Запуск...");
                else System.out.println("Доступ запрещен");
            }
            case "0" -> {
                System.out.println("Выход...");
                return true;
            }
            default -> System.out.println("Неверный выбор.");
        }

        return true;
    }


    private void showAll() {
        if (bdWarehouse.isEmpty()) {
            System.out.println("Нет элементов в базе");
        }

        System.out.println("---- Список грузов ----");

        for (Cargo c : bdWarehouse.getAllCargo()) {
            System.out.println("- " + c.toString());
        }
    }


    private void addCargo() {
        System.out.println("---- Добавить груз ---- ");
        System.out.println("1. Сыпучий (BulkCargo)");
        System.out.println("2. Контейнер (Container)");
        System.out.println("3. Жидкость (Liquid)");
        System.out.println("4. Заморозка (FrozenFood)");
        System.out.println("5. Машины (Car)");
        System.out.print("Выберите тип: ");
        String type = scanner.nextLine().trim();

        System.out.print("Введите название: ");
        String name = scanner.nextLine().trim();

        int id = bdWarehouse.nextId();

        try {
            switch (type) {
                case "1" -> {
                    System.out.print("Введите вес (т): ");
                    double weight = Double.parseDouble(scanner.nextLine());
                    bdWarehouse.add(new BulkCargo(id, name, weight));
                    System.out.println("Добавлено.");
                }
                case "2" -> {
                    System.out.print("Введите вес (т): ");
                    double weight = Double.parseDouble(scanner.nextLine());
                    bdWarehouse.add(new Container(id, name, weight));
                    System.out.println("Добавлено.");
                }
                case "3" -> {
                    System.out.print("Введите вес (т): ");
                    double weight = Double.parseDouble(scanner.nextLine());
                    bdWarehouse.add(new Liquid(id, name, weight));
                    System.out.println("Добавлено.");
                }
                case "4" -> {
                    System.out.print("Введите вес (т): ");
                    double weight = Double.parseDouble(scanner.nextLine());
                    bdWarehouse.add(new FrozenFood(id, name, weight));
                    System.out.println("Добавлено.");
                }
                case "5" -> {
                    System.out.print("Введите количество машин (шт): ");
                    int count = Integer.parseInt(scanner.nextLine());
                    bdWarehouse.add(new Car(id, name, count));
                    System.out.println("Добавлено.");
                }
                default -> System.out.println("Неверный тип.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка ввода: нужно ввести число!");
    }
    }


    private void deleteCargo() {
        System.out.println("---- Удаление груза ---- ");
        System.out.println("Введите ID груза: ");


        try {
            int ID = Integer.parseInt(scanner.nextLine().trim());
            if (bdWarehouse.delete(ID)) {
                System.out.println("Груз успешно удален ");
            } else {
                System.out.println("Не удалось удалить груз");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быт числом. ");
        }
    }



}
