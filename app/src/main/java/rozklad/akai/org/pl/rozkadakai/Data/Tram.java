package rozklad.akai.org.pl.rozkadakai.Data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Tram {

    String departure;
    String direction;
    String line;
    boolean realTime;
    Date departureDate;

    public Tram(String departure, String direction, String line, boolean realTime) {
        this.departure = departure;
        this.direction = direction;
        this.line = line;
        this.realTime = realTime;
        this.departureDate = fromISO8601UTC(departure);
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

    public static Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String toString() {
        return "Line: " + line + " Direction: " + direction + " Departure: " + departure + "\n";
    }

    public Date getDepartureDate() {
        return departureDate;
    }
}
