package rozklad.akai.org.pl.rozkadakai;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import rozklad.akai.org.pl.rozkadakai.Fragments.BikesFragment;
import rozklad.akai.org.pl.rozkadakai.Fragments.BlankFragment;
import rozklad.akai.org.pl.rozkadakai.Fragments.MyBikesFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BikesFragment.OnFragmentInteractionListener,
        BlankFragment.OnFragmentInteractionListener,
        MyBikesFragment.OnFragmentInteractionListener,
        TramsFragment.OnFragmentInteractionListener {

    private FrameLayout fragmentContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Snackbar.make(view, "Add My Stop", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View appBarMain = findViewById(R.id.app_bar_main);
        View contentMain = appBarMain.findViewById(R.id.content_main);
        fragmentContainer = contentMain.findViewById(R.id.fragment_container);
        if (fragmentContainer != null) {
            if (savedInstanceState != null) {
                return;
            }
            BlankFragment blankFragment = BlankFragment.newInstance("", "");
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    blankFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_politechnika) {
            openPutStops("Politechnika");
        } else if (id == R.id.nav_baraniaka) {
            openPutStops("Baraniaka");
        } else if (id == R.id.nav_kornicka) {
            openPutStops("Kórnicka");
        } else if (id == R.id.nav_my_stops) {
            openMyStops();
        } else if (id == R.id.nav_bikes) {
            openBikes();
        } else if (id == R.id.nav_my_bikes) {
            openMyBikes();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openPutStops(String name) {
        if (fragmentContainer != null) {
            TramsFragment tramsFragment = TramsFragment.newInstance(this, "KORN43");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    tramsFragment).commit();
        }
        Toast.makeText(getApplicationContext(), "Open Put Stops: " + name,
                Toast.LENGTH_SHORT).show();
    }

    private void openMyStops() {
        Toast.makeText(getApplicationContext(), "Open My Stops",
                Toast.LENGTH_SHORT).show();
    }

    private void openBikes() {
        if (fragmentContainer != null) {
            BikesFragment bikesFragment = BikesFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    bikesFragment).commit();
        }
        Toast.makeText(getApplicationContext(), "Open Bikes", Toast.LENGTH_SHORT).show();
    }

    private void openMyBikes() {
        if (fragmentContainer != null) {
            MyBikesFragment myBikesFragment = MyBikesFragment.newInstance(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    myBikesFragment).commit();
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
