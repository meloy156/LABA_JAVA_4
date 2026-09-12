package cargo;

public class BulkCargo extends Cargo{
    public BulkCargo(int id, String name, double weight) {
        super(id, name, weight);
    }

    @Override
    public String toString() {
        return String.format("Cargo.BulkCargo{id=%d, name='%s', weight=%.2f t}"
            , id, name, weight);
    }
}
