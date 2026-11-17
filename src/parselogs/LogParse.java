package parselogs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;


public class LogParse {

    /**
     * Метод парсинга директории с логами
     * @param dirPath путь к директории
     * @return {@link List<LogFileResult>} массив c LogFileResult под каждый файл
     * @throws IOException не удалось спарсить файл
     *                     не удалось получить массив файлов в директории
     */
    public static List<LogFileResult> parseDirectory(Path dirPath) throws IOException {
        List<LogFileResult> results;

        try (Stream<Path> listPath = Files.list(dirPath)) {
            results = listPath.filter(Files::isRegularFile)
                    .map(file -> {
                        try {
                            return parseFile(file);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    })
                    .toList();
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }

        return results;
    }


    /**
     * Читаем файл с логами и парсим его к {@link LogFileResult}
     * @param currentPath {@link Path} currentFile
     * @return {@link LogFileResult} result
     * @throws IOException если не удалось прочитать файл, либо не удалось найти в нем все нужные значения
     */
    private static LogFileResult parseFile(Path currentPath) throws IOException {


        String filename = currentPath.getFileName().toString();

        ArrayOrLinked collectionType;
        int size = -1;
        long addAvg = -1, removeAvg = -1, addTotal = -1, removeTotal = -1;



        // 1 тип коллекции
        if (filename.contains("ArrayList")) {
            collectionType = ArrayOrLinked.ARRAY;
        } else if (filename.contains("LinkedList")) {
            collectionType = ArrayOrLinked.LINKED;
        } else {
            throw new IOException("File " + filename + ": неизвестный тип коллекции");
        }

        // все остальное
        try (BufferedReader reader = Files.newBufferedReader(currentPath)) {
            String line;

            while ((line = reader.readLine()) != null) {

                // если строка со значением
                String[] parts = line.split("=");
                if (parts.length != 2) {
                    continue;
                }
                String key = parts[0].trim();
                String value = parts[1].trim();


                // записываем само значение
                try {
                    switch (key) {
                        case "addTotalCount"       ->  size           = Integer.parseInt(value);
                        case "addTotalTime"        ->  addTotal       = Long.parseLong(value);
                        case "addAvgTime"          ->  addAvg         = Long.parseLong(value);
                        case "removeTotalTime"     ->  removeTotal    = Long.parseLong(value);
                        case "removeAvgTime"       ->  removeAvg      = Long.parseLong(value);
                    }
                } catch (NumberFormatException e) {
                    throw new IOException("File " + filename + ": bad number in line " + line, e);
                }
            }


        } catch (IOException e) {
            throw new IOException("File " + filename + ": Не удалось прочитать файл", e);
        }

        if ((size != -1) && (addAvg != -1) && (removeAvg != -1) && (addTotal != -1) && (removeTotal != -1)) {
            return new LogFileResult(collectionType, size, addAvg, removeAvg, addTotal, removeTotal);
        } else {
            throw new IOException("File: " + filename + " :" + "Не удалось получить все значения");
        }
    }
}