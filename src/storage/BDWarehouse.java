package storage;

import cargo.*;
import log.Logger;
import warehouse.Warehouse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class BDWarehouse {

    private final Warehouse currentWarehouse;
    private final String filePath;


    public BDWarehouse(Warehouse currentWarehouse, String filePath) {
        this.currentWarehouse = currentWarehouse;
        this.filePath = filePath;
    }


    // Чтение
    public void loadAll() {
        File f = new File(filePath);
        if (!f.exists()) {
            Logger.info("Файл БД не найден.");
            return;
        }


        int countCargo = 0;
        try ( BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {


            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                Cargo cargo = parseLine(line);
                if (cargo != null) {
                    currentWarehouse.addCargo(cargo);
                    ++countCargo;
                }
            }
            Logger.info("Загруженно из БД: " + countCargo + " грузов.");

        } catch (IOException e) {
            Logger.error("Ошибка чтения БД: " + e.getMessage());
        }
    }





    // Запись
    public void saveAll() {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }


        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {


            for (Cargo c : currentWarehouse.getAllCargo()) {
                writer.write(formatLine(c));
                writer.newLine();
            }
            
            Logger.info("Сохраннено в БД: " + currentWarehouse.size() + " грузов.");
        } catch (IOException e) {
            Logger.error("Ошибка записи в БД: " + e.getMessage());
        }
    }




    // CRUD

    // Добавить
    public void add(Cargo cargo) {
        currentWarehouse.addCargo(cargo);
        Logger.debug("Добавлен в БД:  [ID] = " + cargo.getId());
        saveAll();
    }

    // Найти по ID
    public Cargo getByID(int id) {
        int idx = currentWarehouse.findCargoById(id);
        return idx == -1 ? null : currentWarehouse.getAllCargo().get(id);
    }

    // Удалить по ID
    public boolean delete(int id) {
        if (currentWarehouse.findCargoById(id) == -1)
            return false;

        currentWarehouse.takeCargoById(id);
        Logger.debug("Удален из БД:  [ID] = " + id);
        saveAll();
        return true;
    }

    // Изменение (удаляем старый, добавляем новый)
    public boolean update(int id, Cargo newCargo) {
        if (!delete(id)) return false;
        add(newCargo);
        return true;
    }

    // Генератор нового уникального ID
    public int nextId() {
        int max = 0;
        for (Cargo c : currentWarehouse.getAllCargo()) {
            max = Math.max(max, c.getId());
        }
        return max + 1;
    }



    // Парсинг

    // Объект -> Строка
    private String formatLine(Cargo c) {
        String type = c.getClass().getSimpleName();
        double val = c.getWeight();

        return String.format(java.util.Locale.US, "%s;%d;%s;%.2f", type, c.getId(), c.getName(), val);
    }

    // Строка -> Объект
    private Cargo parseLine(String line) {
        try {
            String[] parts = line.split(";");
            if (parts.length < 4) return null;

            String type = parts[0];
            int id = Integer.parseInt(parts[1]);
            String name = parts[2];
            double val = Double.parseDouble(parts[3]);


            return switch (type) {
                case "BulkCargo"  -> new BulkCargo(id, name, val);
                case "Container"  -> new Container(id, name, val);
                case "Liquid"     -> new Liquid(id, name, val);
                case "FrozenFood" -> new FrozenFood(id, name, val);
                case "Car"        -> new Car(id, name, (int) val);

                default -> {
                    Logger.error("Неизвестный тип груза в БД: " + type);
                    yield null;
                }
            };
        } catch (Exception e) {
            Logger.error("Ошибка парсинга строки БД: " + line + "|" + e.getMessage());
            return null;
        }
    }

    public boolean isEmpty() {
        return currentWarehouse.isEmpty();
    }

    public int size() {
        return currentWarehouse.size();
    }

    public List<Cargo> getAllCargo() {
        return currentWarehouse.getAllCargo();
    }

}
