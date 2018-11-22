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

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Activities.MainActivity;
import rozklad.akai.org.pl.rozkadakai.Adapters.TramsAdapter;
import rozklad.akai.org.pl.rozkadakai.Data.Tram;
import rozklad.akai.org.pl.rozkadakai.DataGetter;
import rozklad.akai.org.pl.rozkadakai.R;

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

    private boolean conected = false;


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
    public static TramsFragment newInstance(MainActivity parent, String stopSymbol, String stopName, boolean conected) {
        TramsFragment fragment = new TramsFragment();
        fragment.setStopSymbol(stopSymbol);
        fragment.setParentActivity(parent);
        fragment.setStopName(stopName);
        fragment.setConnected(conected);
        return fragment;
    }

    private void setConnected(boolean conected) {
        this.conected = conected;
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
        Log.d(KOSSA_LOG, "Symbol: " + stopSymbol);
        if (conected) {
            trams = DataGetter.getTramsDepartures(stopSymbol);
        } else {
            trams = new ArrayList<>();
            Snackbar.make(parentActivity.getFab(), getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
        }
        adapter = new TramsAdapter(trams, parentActivity, stopSymbol);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d(KOSSA_LOG, "TramsFragment " + stopSymbol + ": After run");
        timer = new CountDownTimer(30000, 15000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (conected) {
                    if (trams != null) {
                        trams.clear();
                    }
                    trams = DataGetter.getTramsDepartures(stopSymbol);
                    adapter.setTrams(trams);
                    adapter.notifyDataSetChanged();
                    Log.d(KOSSA_LOG, "TramsFragment " + stopName + " " + stopSymbol + ": Refresh");
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
        Log.d(KOSSA_LOG, "TramsFragment " + stopName + " " + stopSymbol + ": onDetach()");
        if (timer != null) {
            timer.cancel();
        }
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

    public void setTrams(ArrayList<Tram> trams) {
        this.trams = trams;
    }

    public void updateConnectionStatus(boolean connected) {
        Log.d(KOSSA_LOG, "TamsFragment updateConnectionStatus(" + connected + ")");
        this.conected = connected;
        if (connected) {
            if (trams != null)
                trams.clear();
            trams = DataGetter.getTramsDepartures(stopSymbol);
            if (adapter == null) {
                adapter = new TramsAdapter(trams, parentActivity, stopSymbol);
            }
            adapter.setTrams(trams);
            adapter.notifyDataSetChanged();
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

}
