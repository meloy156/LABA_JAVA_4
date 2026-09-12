package train;

import cargo.Cargo;
import wagon.Wagon;
import warehouse.Warehouse;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Train {
    private static final int MAX_WAGONS = 60;

    private final int id;
    private String name;
    private final List<Wagon> wagonList;

    public Train(int id, String name) {
        this.id = id;
        this.name = name;
        this.wagonList = new ArrayList<>();
    }

    // ГЕТЕРЫ
    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    // ФУНКЦИИ
    // Добавление
    public boolean addWagonList(Wagon wagon) {
        if (wagonList.size() >= MAX_WAGONS) {
            return false;
        }
        wagonList.add(wagon);
        return true;
    }

    // Вывод полного массива
    public List<Wagon> getWagonList() {
        return Collections.unmodifiableList(wagonList);
    }

    // кол-во уже лежащих элементов
    public int size() {
        return wagonList.size();
    }

    // проверка на полный
    public boolean isFull() {
        return wagonList.size() >= MAX_WAGONS;
    }

    // общий вес груза
    public double getTotalWeight() {
        double total = 0;
        for (Wagon wagon: wagonList) {
            total += wagon.getCurrentWeight();
        }
        return total;
    }

    /**
     * Загрузка поезда со склада
     * @param Warehouse - склад с которого грузим
     * @return int - колво загруженных грузов
     */
    public int loadFromWarehouse(Warehouse warehouse) {
        int loadCargoCount = 0;

        for (Wagon wagon: wagonList) {
            boolean loadedSomething;

            do {
                loadedSomething = false;

                for (Cargo cargo: warehouse.getAllCargo()) {
                    if(wagon.load(cargo)) {
                        warehouse.removeCargo(cargo);
                        loadCargoCount++;
                        loadedSomething = true;
                        break;
                    }
                }
            } while(loadedSomething);
        }

        return loadCargoCount;
    }


    @Override
    public String toString() {
        return String.format("Train{id=%d, name='%s', wagons=%d, totalWeight=%.2f t"
            , getId(), getName(), size(), getTotalWeight());
    }
}
