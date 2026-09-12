package cargo;

public class Liquid extends Cargo {
    public Liquid(int id, String name, double weight) {
        super(id, name, weight);
    }

    @Override
    public String toString() {
        return String.format("Cargo.Liquid{id=%d, name='%s', weight=%.2f t}"
,id, name, weight);
    }
}
