package rozklad.akai.org.pl.rozkadakai.Data;

public class Tram {

    String departure;
    String direction;
    String line;
    boolean realTime;

    public Tram(String departure, String direction, String line, boolean realTime) {
        this.departure = departure;
        this.direction = direction;
        this.line = line;
        this.realTime = realTime;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDirection() {
        return direction;
    }

    public String getLine() {
        return line;
    }

    public boolean isRealTime() {
        return realTime;
    }

    @Override
    public String toString() {
        return "Line: " + line + " Direction: " + direction + " Departure: " + departure + "\n";
    }
}
