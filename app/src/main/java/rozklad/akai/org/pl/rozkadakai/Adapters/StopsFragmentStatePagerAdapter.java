package rozklad.akai.org.pl.rozkadakai.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Activities.MainActivity;
import rozklad.akai.org.pl.rozkadakai.Data.Stop;
import rozklad.akai.org.pl.rozkadakai.Fragments.TramsFragment;

public class StopsFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<TramsFragment> tramsFragments = new ArrayList<>();
    private Stop stop;
    private MainActivity parentActivity;

    public StopsFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public static StopsFragmentStatePagerAdapter newInstance(FragmentManager fragmentManager, Stop stop, MainActivity parentActivity) {
        StopsFragmentStatePagerAdapter adapter = new StopsFragmentStatePagerAdapter(fragmentManager);
        adapter.setStop(stop);
        adapter.setParentActivity(parentActivity);
        for (int i = stop.getCount() - 1; i >= 0; i--) {
            if (stop.getBoolean(i)) {
                adapter.addTramsFragment(TramsFragment.newInstance(parentActivity, stop.getSymbol(i), stop.getName()));
            }
        }
        return adapter;
    }


    @Override
    public Fragment getItem(int i) {
        return tramsFragments.get(i);
    }

    @Override
    public int getCount() {
        return tramsFragments.size();
    }

    public void addTramsFragment(TramsFragment fragment) {
        tramsFragments.add(fragment);
    }

    private void setStop(Stop stop) {
        this.stop = stop;
    }

    public void setParentActivity(MainActivity parentActivity) {
        this.parentActivity = parentActivity;
    }
}
