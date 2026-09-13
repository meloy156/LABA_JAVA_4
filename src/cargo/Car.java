package cargo;

public class Car extends Cargo {
    private static double weightPerCar = 1.0;

    public  Car(int id, String name, double count) {
        super(id, name, weightPerCar * count);
    }

    @Override
    public String toString() {
        return String.format("Cargo.Car{id=%d, name='%s', count=%.0f}"
                , id, name, weight);
    }
}
