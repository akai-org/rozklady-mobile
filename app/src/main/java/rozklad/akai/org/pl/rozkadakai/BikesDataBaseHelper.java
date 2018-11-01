package rozklad.akai.org.pl.rozkadakai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static rozklad.akai.org.pl.rozkadakai.Constants.KOSSA_LOG;

public class BikesDataBaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "BIKES_TABLE";
    private static final String BIKE_STATION_NAME = "BIKE_STATION_NAME";

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
        String create = "CREATE TABLE " + TABLE_NAME + "(" + BIKE_STATION_NAME + " TEXT UNIQUE)";
        db.execSQL(create);
    }

    public boolean addStationName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BIKE_STATION_NAME, name);
        Log.d(KOSSA_LOG, "Adding new bike station: " + name);

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
            names.add(cursor.getString(0));
        }
        return names;
    }
}
