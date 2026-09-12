package wagon;
import cargo.Cargo;
import cargo.BulkCargo;

public class BulkWagon extends Wagon {
    public BulkWagon(int id) {
        super(id, 60.0);
    }

    @Override
    public boolean canLoad(Cargo cargo) {
        return cargo instanceof BulkCargo;
    }
}
