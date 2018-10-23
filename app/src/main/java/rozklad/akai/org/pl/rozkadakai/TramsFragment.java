package rozklad.akai.org.pl.rozkadakai;

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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import rozklad.akai.org.pl.rozkadakai.Adapters.TramsAdapter;
import rozklad.akai.org.pl.rozkadakai.Data.Tram;

import static rozklad.akai.org.pl.rozkadakai.Constants.KOSSA_LOG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TramsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TramsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TramsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private TramsAdapter adapter;
    private MainActivity parentActivity;
    private CountDownTimer timer;
    private String stopSymbol;
    private String stopName;
    private ArrayList<Tram> trams;

    public TramsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TramsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TramsFragment newInstance(MainActivity parent, String stopSymbol) {
        TramsFragment fragment = new TramsFragment();
        fragment.setStopSymbol(stopSymbol);
        fragment.setParentActivity(parent);

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
        return inflater.inflate(R.layout.fragment_trams, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.trams_recycler_view);
        trams = loadTrams(stopSymbol);
        adapter = new TramsAdapter(trams, parentActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        timer = new CountDownTimer(30000, 15000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                trams = loadTrams(stopSymbol);
                adapter.notifyDataSetChanged();
                Log.d(KOSSA_LOG, "TramsFragment: Refresh");
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
        Log.d(KOSSA_LOG, "TramsFragment: onDetach()");
        timer.cancel();
    }

    public void setStopSymbol(String stopSymbol) {
        this.stopSymbol = stopSymbol;
    }

    public void setParentActivity(MainActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    private ArrayList<Tram> loadTrams(String stopTag) {
        ArrayList<Tram> trams = new ArrayList<>();
        String przystanek = "{\"symbol\":\"" + stopTag + "\"}";
        przystanek = URLEncoder.encode(przystanek);
        String url = "https://www.peka.poznan.pl/vm/method.vm?method=getTimes&p0=" + przystanek;

        HttpPostRequest postRequest = new HttpPostRequest();
        try {
            String responce = postRequest.execute(url).get();

            if (responce != null) {
                JSONObject object = new JSONObject(responce);
                JSONObject succes = object.getJSONObject("success");

                JSONObject bollard = succes.getJSONObject("bollard");
                String name = bollard.getString("name");
                this.setStopName(name);

                JSONArray tramList = succes.getJSONArray("times");

                for (int i = 0; i < tramList.length(); i++) {
                    JSONObject tram = tramList.getJSONObject(i);
                    String line = tram.getString("line");
                    String departure = tram.getString("departure");
                    String direction = tram.getString("direction");
                    boolean realTime = tram.getBoolean("realTime");

                    trams.add(new Tram(departure, direction, line, realTime));
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Null responce", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trams;
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
