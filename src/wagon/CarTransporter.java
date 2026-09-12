package wagon;

import cargo.Car;
import cargo.Cargo;

public class CarTransporter extends Wagon {
    public CarTransporter(int id) {
        super(id, 10.0);
    }

    @Override
    public boolean canLoad(Cargo cargo) {
        return cargo instanceof Car;
    }
}
