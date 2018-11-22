package rozklad.akai.org.pl.rozkadakai.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Adapters.BikesSettingsAdapter;
import rozklad.akai.org.pl.rozkadakai.AddDialogFragment;
import rozklad.akai.org.pl.rozkadakai.Data.Place;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.BikesDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.StopsDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.DataBaseRefreshInterface;
import rozklad.akai.org.pl.rozkadakai.DataGetter;
import rozklad.akai.org.pl.rozkadakai.R;

import static rozklad.akai.org.pl.rozkadakai.Constants.KOSSA_LOG;

public class BikesActivity extends AppCompatActivity implements DataBaseRefreshInterface {

    private boolean saved = true;
    private RecyclerView recyclerView;
    private BikesSettingsAdapter adapter;
    private BikesDataBaseHelper bikesDataBaseHelper;
    private StopsDataBaseHelper stopsDataBaseHelper;
    private ArrayList<Place> places;
    private JSONArray placesArray;
    private boolean connected;
    private FloatingActionButton fab;
    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            connected = ni != null && ni.isConnected();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bikesDataBaseHelper = new BikesDataBaseHelper(this);
        stopsDataBaseHelper = new StopsDataBaseHelper(this);
        places = bikesDataBaseHelper.getStationsWithBooleans();
        recyclerView = findViewById(R.id.bike_settings_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BikesSettingsAdapter(places, this);
        recyclerView.setAdapter(adapter);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
            }
        });
    }

    public void onFabClick() {
        if (connected) {
            placesArray = DataGetter.getBikePlaces();
            DialogFragment dialogFragment = AddDialogFragment
                    .newInstance(placesArray, bikesDataBaseHelper, stopsDataBaseHelper, this);
            dialogFragment.show(getSupportFragmentManager(), "AddDialog");
        } else {
            Snackbar.make(fab, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean saveStations() {
        ArrayList<Place> places = adapter.getPlaces();
        for (Place place : places) {
            bikesDataBaseHelper.updateBooleans(place.getName(), Boolean.valueOf(place.isShow()).toString());

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void refreshData() {
        Log.d(KOSSA_LOG, "Refresh database");
        places = bikesDataBaseHelper.getStationsWithBooleans();
        adapter.setPlaces(places);
        adapter.notifyDataSetChanged();
    }
}
