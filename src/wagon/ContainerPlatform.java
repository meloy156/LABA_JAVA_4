package wagon;

import cargo.Cargo;
import cargo.Container;

public class ContainerPlatform extends Wagon {
    public ContainerPlatform(int id) {
        super(id, 70.0);
    }

    @Override
    public boolean canLoad(Cargo cargo) {
        return cargo instanceof Container;
    }
}
