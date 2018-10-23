package rozklad.akai.org.pl.rozkadakai.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import rozklad.akai.org.pl.rozkadakai.HttpGetRequest;
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

    private TextView poliNameTextView = null;
    private TextView poliCountTextView = null;
    private TextView kornickaNameTextView = null;
    private TextView kornickaCountTextView = null;
    private CountDownTimer timer = null;

    private OnFragmentInteractionListener mListener;

    public BikesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BikesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BikesFragment newInstance() {
        BikesFragment fragment = new BikesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_bikes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        poliNameTextView = view.findViewById(R.id.poli_name_textView);
        poliCountTextView = view.findViewById(R.id.poli_count_textView);
        kornickaCountTextView = view.findViewById(R.id.kornicka_count_textView);
        kornickaNameTextView = view.findViewById(R.id.kornicka_name_textView);
        loadBikes();
        timer = new CountDownTimer(30000, 15000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Log.d(KOSSA_LOG, "Refresh bikes");
                loadBikes();
                this.start();
            }
        }.start();
        super.onViewCreated(view, savedInstanceState);
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
        Log.d(KOSSA_LOG, "BikeFragment: onDetach()");
        timer.cancel();
        mListener = null;
    }

    private void loadBikes() {
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
            JSONObject kornickaObject = (JSONObject) places.get(97);
            JSONArray bikeList = (JSONArray) kornickaObject.get("bike_list");
            //Politechnika Centrum Wykładowe 16
            //Kórnicka  96
            int kornickaCount = getPlaceCountByName("Kórnicka", places);
            int poliCount = getPlaceCountByName("Politechnika Centrum Wykładowe", places);
            poliNameTextView.setText("Politechnika Centrum Wykładowe");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (poliCount <= 2) {
                    poliCountTextView.setTextColor(getContext().getColor(R.color.red));
                } else if (poliCount < 5) {
                    poliCountTextView.setTextColor(getContext().getColor(R.color.orange));

                } else {
                    poliCountTextView.setTextColor(Color.WHITE);
                }
            }
            poliCountTextView.setText("" + poliCount);

            kornickaNameTextView.setText("Kórnicka");
            kornickaCountTextView.setText("" + kornickaCount);

            //Log.d(KOSSA_LOG, object.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getPlaceCountByName(String name, JSONArray places) {
        for (int i = 0; i < places.length(); i++) {
            try {
                JSONObject object = (JSONObject) places.get(i);
                if (object.getString("name").compareTo(name) == 0) {
                    JSONArray bikesList = object.getJSONArray("bike_list");
                    int count = bikesList.length();
                    Log.d(KOSSA_LOG, "i = " + i + " " + object.getString("name") + " count: " + count);
                    return count;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return -1;
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
