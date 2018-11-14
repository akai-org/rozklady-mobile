package rozklad.akai.org.pl.rozkadakai.Data;

import java.util.ArrayList;

public class Stop {

    private String name;
    private ArrayList<String> symbols;
    private boolean[] booleans;

    public Stop(String name, ArrayList<String> symbols) {
        this.name = name;
        this.symbols = symbols;
        this.booleans = new boolean[symbols.size()];
        for (int i = 0; i < booleans.length; i++) {
            booleans[i] = true;
        }
    }

    public Stop(String name, String[] tagsTab, boolean[] booleans) {
        this.name = name;
        this.symbols = new ArrayList<>();
        this.booleans = booleans;
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

    public boolean[] getBooleans() {
        return booleans;
    }

    public void setBoolean(int index, boolean bool) {
        booleans[index] = bool;
    }

    public boolean existsMinOneTrue() {
        for (boolean bool : booleans) {
            if (bool) {
                return true;
            }
        }
        return false;
    }

    public boolean getBoolean(int index) {
        return booleans[index];
    }

    public int getCount() {
        return symbols.size();
    }

    public int getShowCount() {
        int i = 0;
        for (Boolean bool : booleans) {
            if (bool) {
                i++;
            }
        }
        return i;
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
