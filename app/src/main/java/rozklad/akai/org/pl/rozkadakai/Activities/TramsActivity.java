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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import org.json.JSONArray;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Adapters.StopsExpandableListAdapter;
import rozklad.akai.org.pl.rozkadakai.Data.Stop;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.BikesDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.StopsDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.DataGetter;
import rozklad.akai.org.pl.rozkadakai.DialogFragments.BikeAddDialogFragment;
import rozklad.akai.org.pl.rozkadakai.DialogFragments.TramAddDialogFragment;
import rozklad.akai.org.pl.rozkadakai.R;
import rozklad.akai.org.pl.rozkadakai.RefreshInterface;

public class TramsActivity extends AppCompatActivity implements RefreshInterface {

    private ExpandableListView expandableListView;
    private StopsExpandableListAdapter adapter;
    private JSONArray places;
    private BikesDataBaseHelper bikesDataBaseHelper;
    private StopsDataBaseHelper stopsDataBaseHelper;
    private FloatingActionButton fab;
    private boolean isConnected;
    private boolean isFabOpen = false;
    private boolean isFabClicked = false;
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
            places = DataGetter.getBikePlaces();
        } else {
            if (places == null) {
                places = new JSONArray();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


        expandableListView = findViewById(R.id.expandable_stops_listView);
        bikesDataBaseHelper = new BikesDataBaseHelper(this);
        stopsDataBaseHelper = new StopsDataBaseHelper(this);
        ArrayList<Stop> stops = stopsDataBaseHelper.getStops(true);
        adapter = new StopsExpandableListAdapter(stops, getApplicationContext(), this);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (expandableListView.isGroupExpanded(groupPosition)) {
                    expandableListView.collapseGroup(groupPosition);
                } else {
                    expandableListView.expandGroup(groupPosition);
                }
                return true;
            }
        });
        adapter.notifyDataSetChanged();

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
            DialogFragment dialogFragment = BikeAddDialogFragment.newInstance(places, bikesDataBaseHelper, this);
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
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkStateReceiver);
    }

    public boolean saveStops() {
        ArrayList<Stop> stops = adapter.getStops();
        for (Stop stop : stops) {
            String booleans = "";
            for (int i = 0; i < stop.getCount(); i++) {
                booleans += "" + stop.getBoolean(i);
                if (i < stop.getCount() - 1) {
                    booleans += ";";
                }
            }
            if (!stopsDataBaseHelper.updateBooleans(stop.getName(), booleans)) {
                return false;
            }

        }
        return true;
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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void refreshData() {
        ArrayList<Stop> stops = stopsDataBaseHelper.getStops(true);
        adapter.setStops(stops);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshView() {
        closeFab();
    }

    @Override
    public void openView(String name) {
        if (name.compareTo("bikes") == 0) {
            Intent intent = new Intent(this, BikesActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
