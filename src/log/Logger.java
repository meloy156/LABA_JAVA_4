package log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static String logFilePath = "data/app.log";
    private static boolean debugMode = false;


    public static void setLogFile(String path) {
        logFilePath = path;
    }
    public static void setDebugMode(boolean mode) {
        debugMode = mode;
    }

    public static void info(String message) {
        write("INFO", message);
    }


    public static void error(String message) {
        write("ERROR", message);
    }

    public static void debug(String message) {
        if (debugMode) {
            write("DEBUG", message);
        }
    }



    private static void write(String levelLog, String message) {

        String stringLogEntry = String.format("[%s] [%s] %s"
                                        , LocalDateTime.now().format(FMT)
                                        , levelLog
                                        , message);


        File logFile = new File(logFilePath);
        if (logFile.getParentFile() != null) {
            logFile.getParentFile().mkdirs();
        }


        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(logFilePath, true), StandardCharsets.UTF_8))) {

            writer.write(stringLogEntry);
            writer.newLine();

        } catch (IOException e) {

            System.err.println("Ошибка Логгер: не удалось записать в файл " + logFilePath);
        }
    }
}
