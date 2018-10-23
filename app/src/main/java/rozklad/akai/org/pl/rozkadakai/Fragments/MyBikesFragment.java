package rozklad.akai.org.pl.rozkadakai.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import rozklad.akai.org.pl.rozkadakai.Adapters.MyBikesAdapter;
import rozklad.akai.org.pl.rozkadakai.Data.Place;
import rozklad.akai.org.pl.rozkadakai.HttpGetRequest;
import rozklad.akai.org.pl.rozkadakai.MainActivity;
import rozklad.akai.org.pl.rozkadakai.R;

import static rozklad.akai.org.pl.rozkadakai.Constants.KOSSA_LOG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyBikesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyBikesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBikesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private MyBikesAdapter adapter;
    private MainActivity parentActivity;
    private CountDownTimer timer;
    private ArrayList<Place> places;

    public MyBikesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyBikesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyBikesFragment newInstance(MainActivity parentActivity) {
        MyBikesFragment fragment = new MyBikesFragment();
        fragment.setParentActivity(parentActivity);
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

        places = loadMyPlaces();
        adapter = new MyBikesAdapter(places, parentActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        timer = new CountDownTimer(30000, 15000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                places = loadMyPlaces();
                adapter.notifyDataSetChanged();
                Log.d(KOSSA_LOG, "MyBikesFragment: Refresh");
                this.start();
            }
        }.start();
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    private ArrayList<Place> loadMyPlaces() {
        ArrayList<Place> list = new ArrayList<>();
        String[] names = {"Murawa / Słowiańska", "Brzask / Międzychodzka", "Kórnicka"};
        int[] count = getBikesCount(names);
        for (int i = 0; i < names.length; i++) {
            list.add(new Place(names[i], count[i]));
        }
        return list;
    }

    private int[] getBikesCount(String[] names) {
        int count[] = new int[names.length];
        try {
            String myUrl = "https://api.nextbike.net/maps/nextbike-live.json?city=192";
            HttpGetRequest getRequest = new HttpGetRequest();
            String responce = getRequest.execute(myUrl).get();
            //Log.d(KOSSA_LOG, "Responce: " + responce);
            JSONObject object = new JSONObject(responce);
            JSONArray countriesArray = object.getJSONArray("countries");
            JSONObject poznanObject = (JSONObject) countriesArray.get(0);
            JSONArray citiesArray = (JSONArray) poznanObject.get("cities");
            JSONObject cityObject = (JSONObject) citiesArray.get(0);
            JSONArray places = (JSONArray) cityObject.get("places");
            for (int i = 0; i < names.length; i++) {
                for (int j = 0; j < places.length(); j++) {
                    try {
                        JSONObject obj = (JSONObject) places.get(j);
                        if (obj.getString("name").compareTo(names[i]) == 0) {
                            JSONArray bikesList = obj.getJSONArray("bike_list");
                            count[i] = bikesList.length();
                            Log.d(KOSSA_LOG, "i = " + i + " " + obj.getString("name") + " count: " + count);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return count;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
