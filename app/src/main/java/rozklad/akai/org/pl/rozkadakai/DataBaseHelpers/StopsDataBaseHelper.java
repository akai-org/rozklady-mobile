package rozklad.akai.org.pl.rozkadakai.DataBaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Data.Stop;

import static rozklad.akai.org.pl.rozkadakai.Constants.KOSSA_LOG;

public class StopsDataBaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "STOPS_TABLE";
    private static final String STOP_NAME = "STOP_NAME";
    private static final String STOP_TAG = "STOP_TAG";

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
        String create = "CREATE TABLE " + TABLE_NAME + "(" + STOP_NAME + " TEXT UNIQUE, " + STOP_TAG + " TEXT UNIQUE)";
        db.execSQL(create);
    }

    public boolean addStop(Stop stop) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STOP_NAME, stop.getName());
        contentValues.put(STOP_TAG, stop.getTags());
        Log.d(KOSSA_LOG, "Adding new stop: " + stop.getName() + "[" + stop.getTags() + "]");

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<Stop> getStops() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Stop> stops = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String tags = cursor.getString(1);
            String[] tagsTab = tags.split(";");
            stops.add(new Stop(name, tagsTab));
        }
        return stops;
    }
}
