package rozklad.akai.org.pl.rozkadakai.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Activities.MainActivity;
import rozklad.akai.org.pl.rozkadakai.Adapters.BikesAdapter;
import rozklad.akai.org.pl.rozkadakai.Data.Place;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.BikesDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.DataGetter;
import rozklad.akai.org.pl.rozkadakai.R;

import static rozklad.akai.org.pl.rozkadakai.Constants.KOSSA_LOG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BikesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BikesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BikesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private TextView nameTextView;
    private BikesAdapter adapter;
    private MainActivity parentActivity;
    private CountDownTimer timer;
    private ArrayList<Place> placesArrayList;
    private JSONArray places;
    private ArrayList<String> names;
    private BikesDataBaseHelper bikesDataBaseHelper = null;
    private boolean my;
    private boolean connected;

    public BikesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BikesFragment.
     */
    public static BikesFragment newInstance(MainActivity parentActivity, BikesDataBaseHelper bikesDataBaseHelper, JSONArray places, boolean my, boolean connected) {
        BikesFragment fragment = new BikesFragment();
        fragment.setParentActivity(parentActivity);
        fragment.setBikesDataBaseHelper(bikesDataBaseHelper);
        fragment.setNames(bikesDataBaseHelper.getStationsNames());
        fragment.setPlaces(places);
        fragment.setMain(my);
        fragment.setConnected(connected);
        return fragment;
    }

    public static BikesFragment newInstance(MainActivity parentActivity, ArrayList<String> names, JSONArray places, boolean my, boolean connected) {
        BikesFragment fragment = new BikesFragment();
        fragment.setParentActivity(parentActivity);
        fragment.setNames(names);
        fragment.setPlaces(places);
        fragment.setMain(my);
        fragment.setConnected(connected);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bikes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.my_bikes_recycler_view);
        nameTextView = view.findViewById(R.id.places_name_textView);
        if (my) {
            nameTextView.setText(parentActivity.getText(R.string.saved_stations));
        }
        placesArrayList = loadMyPlaces();
        adapter = new BikesAdapter(placesArrayList, parentActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        timer = new CountDownTimer(30000, 15000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (connected) {
                    places = DataGetter.getBikePlaces();
                    placesArrayList = loadMyPlaces();
                    adapter.setPlaces(placesArrayList);
                    adapter.notifyDataSetChanged();
                    Log.d(KOSSA_LOG, "BikesFragment: Refresh");
                } else {
                    Snackbar.make(parentActivity.getFab(), getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
                }
                this.start();
            }
        }.start();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        timer.cancel();
    }

    public void setParentActivity(MainActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public void setBikesDataBaseHelper(BikesDataBaseHelper bikesDataBaseHelper) {
        this.bikesDataBaseHelper = bikesDataBaseHelper;
    }

    public void setPlaces(JSONArray places) {
        this.places = places;
    }

    public void setMain(boolean my) {
        this.my = my;
    }

    private ArrayList<Place> loadMyPlaces() {
        ArrayList<Place> list = new ArrayList<>();
        if (connected) {
            if (bikesDataBaseHelper != null) {
                names = bikesDataBaseHelper.getStationsNames();
            }

            for (int i = 0; i < names.size(); i++) {
                list.add(DataGetter.getPlaceByName(places, names.get(i)));
            }
        }
        return list;
    }

    public void updateConnectionStatus(boolean connected) {
        Log.d(KOSSA_LOG, "BikesFragment updateConnectionStatus(" + connected + ")");
        this.connected = connected;
        if (connected) {
            places = DataGetter.getBikePlaces();
            placesArrayList = loadMyPlaces();
            adapter.setPlaces(placesArrayList);
            adapter.notifyDataSetChanged();
            Log.d(KOSSA_LOG, "BikesFragment: Refresh");
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
