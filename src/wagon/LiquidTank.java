package wagon;

import cargo.Cargo;
import cargo.Liquid;

public class LiquidTank extends Wagon {
    public LiquidTank(int id) {
        super(id, 60.0);
    }

    @Override
    public boolean canLoad(Cargo cargo) {
        return cargo instanceof Liquid;
    }
}
