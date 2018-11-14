package rozklad.akai.org.pl.rozkadakai.Data;

import java.util.ArrayList;

public class Stop {

    private String name;
    private ArrayList<String> symbols;

    public Stop(String name, ArrayList<String> symbol) {
        this.name = name;
        this.symbols = symbol;
    }

    public Stop(String name, String[] tagsTab) {
        this.name = name;
        this.symbols = new ArrayList<>();
        for (int i = 0; i < tagsTab.length; i++) {
            symbols.add(tagsTab[i]);
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getSymbols() {
        return symbols;
    }

    public int getCount() {
        return symbols.size();
    }

    public String getSymbol(int position) {
        return symbols.get(position);
    }

    public String getTags() {
        String tags = "";
        for (int i = 0; i < symbols.size(); i++) {
            tags += symbols.get(i);
            if (i < symbols.size() - 1) {
                tags += ";";
            }
        }
        return tags;
    }
}
