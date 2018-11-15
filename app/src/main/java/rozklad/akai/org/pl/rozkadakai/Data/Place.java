package rozklad.akai.org.pl.rozkadakai.Data;

public class Place {

    private String name;
    private int count;
    private boolean show;

    public Place(String name, int count) {
        this.name = name;
        this.count = count;
        show = true;
    }

    public Place(String name, boolean show) {
        this.name = name;
        this.show = show;
    }

    public boolean isShow() {
        return show;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
