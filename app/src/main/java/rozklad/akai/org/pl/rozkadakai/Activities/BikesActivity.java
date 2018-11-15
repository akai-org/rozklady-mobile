package rozklad.akai.org.pl.rozkadakai.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Adapters.BikesSettingsAdapter;
import rozklad.akai.org.pl.rozkadakai.Data.Place;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.BikesDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.R;

public class BikesActivity extends AppCompatActivity {

    private boolean saved = true;
    private RecyclerView recyclerView;
    private BikesSettingsAdapter adapter;
    private BikesDataBaseHelper bikesDataBaseHelper;
    private ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bikesDataBaseHelper = new BikesDataBaseHelper(this);
        places = bikesDataBaseHelper.getStationsWithBooleans();
        recyclerView = findViewById(R.id.bike_settings_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BikesSettingsAdapter(places, this);
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveStations()) {
                    Snackbar.make(view, "Successful saved", Snackbar.LENGTH_LONG).show();
                    saved = true;
                } else {
                    Snackbar.make(view, "Error by saving", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            closeActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeActivity() {
        if (!saved) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.not_saved_message);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saved = true;
                    closeActivity();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            finish();
        }
    }


    private boolean saveStations() {
        ArrayList<Place> places = adapter.getPlaces();
        for (Place place : places) {
            bikesDataBaseHelper.updateBooleans(place.getName(), Boolean.valueOf(place.isShow()).toString());

        }
        return true;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
