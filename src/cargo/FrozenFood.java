package cargo;

public class FrozenFood extends Cargo {
    public FrozenFood(int id, String name, double weight) {
        super(id, name, weight);
    }

    @Override
    public String toString() {
        return String.format("Cargo.FrozenFood{id=%d, name='%s', weight=%.2f t}"
                , id, name, weight);
    }
}
