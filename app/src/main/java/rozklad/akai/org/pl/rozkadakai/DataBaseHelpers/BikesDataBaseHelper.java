package rozklad.akai.org.pl.rozkadakai.DataBaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Data.Place;

public class BikesDataBaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "BIKES_TABLE";
    private static final String BIKE_STATION_NAME = "BIKE_STATION_NAME";
    private static final String IS_SHOW = "IS_SHOW";

    public BikesDataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "DROP IF TABLE EXISTS BIKES_TABLE";
        db.execSQL(drop);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TABLE_NAME + "(" + BIKE_STATION_NAME + " TEXT UNIQUE," +
                IS_SHOW + " TEXT )";
        db.execSQL(create);
    }

    public boolean addStationName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BIKE_STATION_NAME, name);
        contentValues.put(IS_SHOW, "true");
        //Log.d(LOG_TAG, "Adding new bike station: " + name);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<String> getStationsNames() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> names = new ArrayList<>();
        while (cursor.moveToNext()) {
            boolean show = Boolean.parseBoolean(cursor.getString(1));
            if (show) {
                names.add(cursor.getString(0));
            }
        }
        return names;
    }

    public ArrayList<Place> getStationsWithBooleans() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Place> places = new ArrayList<>();
        while (cursor.moveToNext()) {
            boolean show = Boolean.parseBoolean(cursor.getString(1));
            String name = cursor.getString(0);
            places.add(new Place(name, show));
        }
        return places;
    }


    public boolean updateBooleans(String name, String bool) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_SHOW, bool);
        String[] names = {name};
        int result = db.update(TABLE_NAME, values, BIKE_STATION_NAME + " = ?", names);
        if (result != 1) {
            return false;
        } else {
            return true;
        }
    }
}
