package rozklad.akai.org.pl.rozkadakai.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Objects;

import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.BikesDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.DataGetter;
import rozklad.akai.org.pl.rozkadakai.R;
import rozklad.akai.org.pl.rozkadakai.RefreshInterface;

public class BikeAddDialogFragment extends DialogFragment {

    private JSONArray places;
    private BikesDataBaseHelper bikesDataBaseHelper;
    private RefreshInterface refreshInterface;
    private AlertDialog dialog;
    private View dialogView;
    private AutoCompleteTextView nameEditText;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() % 3 == 2) {
                ArrayList<String> names = DataGetter.getPlacesNamesByPattern(s.toString(), places);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
                nameEditText.setAdapter(adapter);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            ArrayList<String> names = DataGetter.getPlacesNamesByPattern(s.toString(), places);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
            nameEditText.setAdapter(adapter);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setEnabled(contains(names, s.toString()));

        }
    };

    public static BikeAddDialogFragment newInstance(JSONArray places, BikesDataBaseHelper bikesDataBaseHelper,
                                                    RefreshInterface refreshInterface) {
        BikeAddDialogFragment bikeAddDialogFragment = new BikeAddDialogFragment();
        bikeAddDialogFragment.setBikesDataBaseHelper(bikesDataBaseHelper);
        bikeAddDialogFragment.setRefreshInterface(refreshInterface);
        bikeAddDialogFragment.setPlaces(places);

        return bikeAddDialogFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();

        dialogView = inflater.inflate(R.layout.bike_add_dialog_fragment, null);

        nameEditText = dialogView.findViewById(R.id.name_editText);
        nameEditText.addTextChangedListener(textWatcher);


        builder.setView(dialogView);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addBikesStation(nameEditText.getText().toString());
                refreshInterface.refreshData();
                refreshInterface.refreshView();
                refreshInterface.openView("bikes");
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BikeAddDialogFragment.this.getDialog().cancel();
                refreshInterface.refreshView();
            }
        });
        dialog = builder.create();


        return dialog;
    }

    private void addBikesStation(String name) {
        boolean answer = bikesDataBaseHelper.addStationName(name);
        if (answer) {
            Toast.makeText(getContext(),
                    getString(R.string.successful_adding_bike_station) + name,
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.error_by_adding_bike_station),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void setRefreshInterface(RefreshInterface refreshInterface) {
        this.refreshInterface = refreshInterface;
    }

    public void setBikesDataBaseHelper(BikesDataBaseHelper bikesDataBaseHelper) {
        this.bikesDataBaseHelper = bikesDataBaseHelper;
    }

    public void setPlaces(JSONArray places) {
        this.places = places;
    }

    private boolean contains(ArrayList<String> names, String string) {
        for (String name : names) {
            if (name.compareTo(string) == 0) {
                return true;
            }
        }
        return false;
    }
}
