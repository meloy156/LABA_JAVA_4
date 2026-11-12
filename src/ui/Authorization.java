package ui;

import config.ConfigLoader;
import log.Logger;

import java.io.PrintStream;
import java.util.Scanner;

public class Authorization {

    public static boolean login(ConfigLoader config, Scanner scanner, PrintStream printStream) {
        printStream.println("---- Вход в систему ----");
        printStream.println("Логин: ");
        String userLogin = scanner.nextLine();

        printStream.println("Пароль: ");
        String userPassword = scanner.nextLine();


        // Проверка логина и пароля
        if (userLogin.equals(config.getLogin()) && userPassword.equals(config.getPassword())) {
            printStream.println(" Вход выполнен -- " + userLogin);
            Logger.info("Успешный вход. Пользователь: " + userLogin + " Группа: " + config.getGroup());
            return true;
        } else {
            printStream.println("Неверный логин или пароль. ");
            Logger.error("Неудачная попытка входа. Логин: " + userLogin);
            return false;
        }
    }
}
