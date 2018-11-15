package rozklad.akai.org.pl.rozkadakai.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Adapters.StopsExpandableListAdapter;
import rozklad.akai.org.pl.rozkadakai.Data.Stop;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.StopsDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.R;

public class TramsActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private StopsExpandableListAdapter adapter;
    private StopsDataBaseHelper stopsDataBaseHelper;
    private boolean saved = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (saveStops()) {
                    Snackbar.make(view, "Successful saved", Snackbar.LENGTH_LONG).show();
                    saved = true;
                } else {
                    Snackbar.make(view, "Error by saving", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        expandableListView = findViewById(R.id.expandable_stops_listView);
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
        // setOnGroupClickListener listener for group heading click
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

    public void setSaved(boolean saved) {
        this.saved = saved;
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
            closeActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        closeActivity();
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
}
