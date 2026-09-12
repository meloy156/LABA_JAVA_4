package wagon;
import cargo.Cargo;
import cargo.FrozenFood;

public class RefrigeratorWagon extends Wagon {
    public RefrigeratorWagon(int id) {
        super(id, 15.0);
    }

    @Override
    public boolean canLoad(Cargo cargo) {
        return cargo instanceof FrozenFood;
    }
}
