package rozklad.akai.org.pl.rozkadakai.Data;

import java.util.ArrayList;

public class Stop {

    private String name;
    private ArrayList<String> symbol;

    public Stop(String name, ArrayList<String> symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getSymbol() {
        return symbol;
    }

    public int getCount() {
        return symbol.size();
    }

    public String getSymbol(int position) {
        return symbol.get(position);
    }
}
