package cargo;

public class Container extends Cargo {
    public Container(int id, String name, double weight) {
        super(id, name, weight);
    }

    @Override
    public String toString() {
        return String.format("Cargo.Container{id=%d, name='%s', weight=%.2f t}"
            , id, name, weight);
    }
}
