package rozklad.akai.org.pl.rozkadakai.Activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import rozklad.akai.org.pl.rozkadakai.Data.Place;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.BikesDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.StopsDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.DataGetter;
import rozklad.akai.org.pl.rozkadakai.DialogFragments.BikeAddDialogFragment;
import rozklad.akai.org.pl.rozkadakai.DialogFragments.TramAddDialogFragment;
import rozklad.akai.org.pl.rozkadakai.R;
import rozklad.akai.org.pl.rozkadakai.RefreshInterface;

import static rozklad.akai.org.pl.rozkadakai.Constants.KOSSA_LOG;

public class BikesActivity extends AppCompatActivity implements RefreshInterface {

    private boolean saved = true;
    private RecyclerView recyclerView;
    private BikesSettingsAdapter adapter;
    private BikesDataBaseHelper bikesDataBaseHelper;
    private StopsDataBaseHelper stopsDataBaseHelper;
    private ArrayList<Place> places;
    private JSONArray placesArray;
    private boolean isConnected;
    private boolean isFabOpen = false;
    private boolean isFabClicked = false;
    private FloatingActionButton fab;
    private FloatingActionButton bikeFab;
    private FloatingActionButton tramFab;
    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            isConnected = ni != null && ni.isConnected();
            refreshPlaces();
        }
    };

    private void refreshPlaces() {
        if (isConnected) {
            placesArray = DataGetter.getBikePlaces();
        } else {
            if (places == null) {
                placesArray = new JSONArray();
            }
        }
    }

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


        fab = findViewById(R.id.fab);
        bikeFab = findViewById(R.id.bike_fab);
        tramFab = findViewById(R.id.tram_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
            }
        });
        bikeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBikeFabClick();

            }
        });
        tramFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTramFabClick();
            }
        });

    }

    private void onTramFabClick() {
        if (isConnected) {
            isFabClicked = true;
            DialogFragment dialogFragment = TramAddDialogFragment.newInstance(stopsDataBaseHelper, this);
            dialogFragment.show(getSupportFragmentManager(), "TramAddDialog");
        } else {
            Snackbar.make(fab, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            closeFab();
        }
    }

    private void onBikeFabClick() {
        if (isConnected) {
            isFabClicked = true;
            DialogFragment dialogFragment = BikeAddDialogFragment.newInstance(placesArray, bikesDataBaseHelper, this);
            dialogFragment.show(getSupportFragmentManager(), "BikeAddDialog");

        } else {
            Snackbar.make(fab, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            closeFab();
        }
    }

    @SuppressLint("RestrictedApi")
    void onFabClick() {
        if (!isFabOpen) {
            CountDownTimer timer = new CountDownTimer(2000, 100) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (isFabOpen && !isFabClicked) {
                        closeFab();
                    }
                }
            };
            timer.start();
            openFab();
        } else {
            closeFab();
        }
    }

    private void closeFab() {
        bikeFab.animate().translationY(0);
        tramFab.animate().translationY(0);
        isFabOpen = false;
        isFabClicked = false;
    }

    private void openFab() {
        bikeFab.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        tramFab.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        isFabOpen = true;
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

    @Override
    public void refreshView() {
        closeFab();
    }

    @Override
    public void openView(String name) {
        if (name.compareTo("trams") == 0) {
            Intent intent = new Intent(this, TramsActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
