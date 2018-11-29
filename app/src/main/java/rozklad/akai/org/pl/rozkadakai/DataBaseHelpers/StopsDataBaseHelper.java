package rozklad.akai.org.pl.rozkadakai.DataBaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Data.Stop;

public class StopsDataBaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "STOPS_TABLE";
    private static final String STOP_NAME = "STOP_NAME";
    private static final String STOP_TAG = "STOP_TAG";
    private static final String TAG_BOOLEANS = "TAG_BOOLEANS";

    public StopsDataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "DROP IF TABLE EXISTS " + TABLE_NAME;
        db.execSQL(drop);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TABLE_NAME + "(" + STOP_NAME + " TEXT UNIQUE, " +
                STOP_TAG + " TEXT UNIQUE, " + TAG_BOOLEANS + " TEXT)";
        db.execSQL(create);
    }

    public boolean addStop(Stop stop) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STOP_NAME, stop.getName());
        contentValues.put(STOP_TAG, stop.getTags());
        String booleans = "";
        for (int i = 0; i < stop.getCount(); i++) {
            booleans += "true";
            if (i < stop.getCount() - 1) {
                booleans += ";";
            }
        }
        contentValues.put(TAG_BOOLEANS, booleans);
        //Log.d(LOG_TAG, "Adding new stop: " + stop.getName() + "[" + stop.getTags() + "]");

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<Stop> getStops(boolean toSettings) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Stop> stops = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String tags = cursor.getString(1);
            String booleans = cursor.getString(2);
            String[] booleansStringTab = booleans.split(";");
            boolean[] bools = new boolean[booleansStringTab.length];
            for (int i = 0; i < booleansStringTab.length; i++) {
                bools[i] = Boolean.parseBoolean(booleansStringTab[i]);
            }
            String[] tagsTab = tags.split(";");
            Stop stop = new Stop(name, tagsTab, bools);
            if (stop.existsMinOneTrue() || toSettings) {
                stops.add(stop);
            }
        }
        return stops;
    }

    public boolean updateBooleans(String name, String booleans) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TAG_BOOLEANS, booleans);
        String[] names = {name};
        int result = db.update(TABLE_NAME, values, STOP_NAME + " = ?", names);
        if (result != 1) {
            return false;
        } else {
            return true;
        }
    }
}
