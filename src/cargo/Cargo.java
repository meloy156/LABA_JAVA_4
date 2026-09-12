package cargo;

public abstract class Cargo {
    protected int id;
    protected String name;
    protected double weight;

    public Cargo(int id, String name, double weight){
        this.id = id;
        this.name = name;
        this.weight = weight;
    }



    public int getId() {return id;}
    public String getName() {return name;}
    public double getWeight() {return weight;}

    @Override
    public String toString() {
        return String.format("Cargo{id=%d, name='%s', weight=%.2f t}",
id, name, weight);
    }

}
