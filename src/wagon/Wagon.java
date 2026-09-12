package wagon;

import cargo.Cargo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Wagon {
    protected int id;
    protected double maxWeight;
    protected double currentWeight;
    protected List<Cargo> loadedCargo;

    public Wagon(int id, double maxWeight) {
        this.id = id;
        this.maxWeight = maxWeight;
        this.currentWeight = 0.0;
        this.loadedCargo = new ArrayList<>();
    }

    public int getId() { return id; }
    public double getMaxWeight() { return maxWeight; }
    public double getCurrentWeight() { return currentWeight; }
    public List<Cargo> getLoadedCargo() {
        return Collections.unmodifiableList(loadedCargo);
    }
    public double getFreeSpace() { return maxWeight - currentWeight; }


    protected abstract boolean canLoad(Cargo cargo);

    /**
     * Можно ли загрузить этот груз
     * @param cargo груз который хотим загрузить
     * @return false - не подходит ... true - подходит и загрузили
     */
    public boolean load(Cargo cargo) {
        if (!canLoad(cargo)) {
            return false;
        }
        if (cargo.getWeight() > getFreeSpace()) {
            return false;
        }

        loadedCargo.add(cargo);
        currentWeight += cargo.getWeight();
        return true;
    }

    @Override
    public String toString() {
        return String.format("Wagon{id=%d, maxWeight=%.2f, currentWeight=%.2f, loaded=%d}",
                id, maxWeight, currentWeight, loadedCargo.size());
    }

}
