package rozklad.akai.org.pl.rozkadakai.Data;

import java.util.ArrayList;

public class Stop {

    private String name;
    private ArrayList<String> symbol;

    public Stop(String name, ArrayList<String> symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public Stop(String name, String[] tagsTab) {
        this.name = name;
        this.symbol = new ArrayList<>();
        for (int i = 0; i < tagsTab.length; i++) {
            symbol.add(tagsTab[i]);
        }
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

    public String getTags() {
        String tags = "";
        for (int i = 0; i < symbol.size(); i++) {
            tags += symbol.get(i);
            if (i < symbol.size() - 1) {
                tags += ";";
            }
        }
        return tags;
    }
}
