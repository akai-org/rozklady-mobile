package rozklad.akai.org.pl.rozkadakai.Data;

public class Place {

    private String name;
    private int count;

    public Place(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
}
