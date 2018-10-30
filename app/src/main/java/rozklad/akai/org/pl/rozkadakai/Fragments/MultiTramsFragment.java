package rozklad.akai.org.pl.rozkadakai.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rozklad.akai.org.pl.rozkadakai.Adapters.StopsFragmentStatePagerAdapter;
import rozklad.akai.org.pl.rozkadakai.Data.Stop;
import rozklad.akai.org.pl.rozkadakai.MainActivity;
import rozklad.akai.org.pl.rozkadakai.R;

import static rozklad.akai.org.pl.rozkadakai.Constants.KOSSA_LOG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MultiTramsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MultiTramsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultiTramsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private StopsFragmentStatePagerAdapter pagerAdapter;
    private TextView stopNameTextView;
    private Stop stop;
    private MainActivity parentActivity;
    private ViewPager vpPager;

    public MultiTramsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MultiTramsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MultiTramsFragment newInstance(MainActivity parentActivity, Stop stop) {
        MultiTramsFragment fragment = new MultiTramsFragment();
        fragment.setParentActivity(parentActivity);
        fragment.setStop(stop);
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
        return inflater.inflate(R.layout.fragment_multi_trams, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vpPager = (ViewPager) view.findViewById(R.id.vpPager);
        stopNameTextView = view.findViewById(R.id.stop_name_textView);
        stopNameTextView.setText(stop.getName());
        pagerAdapter = StopsFragmentStatePagerAdapter.newInstance(getFragmentManager(), stop, parentActivity);
        vpPager.setAdapter(pagerAdapter);
        vpPager.setClipToPadding(false);
        vpPager.setPageMargin(12);
        pagerAdapter.notifyDataSetChanged();
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

        for (int i = 0; i < stop.getCount(); i++) {
            pagerAdapter.getItem(i).onDetach();
        }
        mListener = null;
        super.onDetach();
        Log.d(KOSSA_LOG, "MultiTramsFragment " + stop.getName() + ": onDetach()");
    }

    public void setParentActivity(MainActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
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
