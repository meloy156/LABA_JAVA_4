package warehouse;

import cargo.Cargo;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Warehouse {
    private final int id;
    private String name;
    private final List<Cargo> cargoList;

    public Warehouse(int id, String name) {
        this.id = id;
        this.name = name;
        cargoList = new ArrayList<>();
    }

    // ГЕТТЕРЫ
    public int getId() { return id; }
    public String getName() { return name;}

    // СЕТТЕРЫ
    public void setName(String name) { this.name = name; }


    //  ФУНКЦИИ
    // Базовые методы
    public void addCargo(Cargo cargo) { cargoList.add(cargo); }
    public boolean removeCargo(Cargo cargo) { return cargoList.remove(cargo); }
    public int size() { return cargoList.size(); }
    public boolean isEmpty() { return cargoList.isEmpty(); }
    public List<Cargo> getAllCargo() {return Collections.unmodifiableList(cargoList);}

    /** Поиск груза по имени/айди/типу
        * @return индекс или -1, если не найден
     */
    public int findCargoById(int id) {
        for(int i = 0; i < cargoList.size(); ++i) {
            if (cargoList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public int findCargoByName(String name) {
        for(int i = 0; i < cargoList.size(); ++i) {
            if (cargoList.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
    public int findCargoByType(Class<? extends Cargo> type) {
        for(int i = 0; i < cargoList.size(); ++i) {
            if (type.isInstance(cargoList.get(i))) {
                return i;
            }
        }
        return -1;
    }


    /** Взятие груза и удаление со склада
     * @return груз(тип: Cargo) или null
     */
    public Cargo takeCargo() {
        if (cargoList.isEmpty()) {
            return null;
        }
        return cargoList.removeFirst();
    }
    public Cargo takeCargoById(int id) {
        int index = findCargoById(id);
        if (index == -1) {
            return null;
        }
        return cargoList.remove(index);
    }
    public Cargo takeCargoByName(String name) {
        int index = findCargoByName(name);
        if (index == -1) {
            return null;
        }
        return cargoList.remove(index);
    }
    public Cargo takeCargoByType(Class<? extends Cargo> type) {
        int index = findCargoByType(type);
        if (index == -1) {
            return null;
        }
        return cargoList.remove(index);
    }

    @Override
    public String toString() {
        return String.format("Warehouse{id=%d, name='%s', cargoCount=%d}"
            , getId(), getName(), size());
    }
}