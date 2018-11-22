package rozklad.akai.org.pl.rozkadakai;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Data.Stop;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.BikesDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.StopsDataBaseHelper;

public class AddDialogFragment extends DialogFragment {

    private AlertDialog dialog;
    private View addDialogFragmentView;
    private AutoCompleteTextView nameEditText;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private boolean tramStop = true;
    private JSONArray places;
    private BikesDataBaseHelper bikesDataBaseHelper;
    private StopsDataBaseHelper stopsDataBaseHelper;
    private Activity parentActivity;
    private DataBaseRefreshInterface dataBaseRefreshInterface;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() % 3 == 2) {
                if (tramStop) {
                    ArrayList<String> names = DataGetter.getStopsByPattern(s.toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
                    nameEditText.setAdapter(adapter);
                } else {
                    ArrayList<String> names = DataGetter.getPlacesNamesByPattern(s.toString(), places);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
                    nameEditText.setAdapter(adapter);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (tramStop) {
                ArrayList<String> names = DataGetter.getStopsByPattern(s.toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
                nameEditText.setAdapter(adapter);

                boolean cont = contains(names, s.toString());
                if (cont) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            } else {
                ArrayList<String> names = DataGetter.getPlacesNamesByPattern(s.toString(), places);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
                nameEditText.setAdapter(adapter);
                boolean cont = contains(names, s.toString());
                if (cont) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        }
    };

    public AddDialogFragment() {
        super();
    }

    public static AddDialogFragment newInstance(JSONArray places,
                                                BikesDataBaseHelper bikesDataBaseHelper,
                                                StopsDataBaseHelper stopsDataBaseHelper, DataBaseRefreshInterface dataBaseRefreshInterface) {
        AddDialogFragment addDialogFragment = new AddDialogFragment();
        addDialogFragment.setPlaces(places);
        addDialogFragment.setBikesDataBaseHelper(bikesDataBaseHelper);
        addDialogFragment.setStopsDataBaseHelper(stopsDataBaseHelper);
        addDialogFragment.setDataBaseRefreshInterface(dataBaseRefreshInterface);

        return addDialogFragment;
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

        LayoutInflater inflater = getActivity().getLayoutInflater();

        addDialogFragmentView = inflater.inflate(R.layout.add_dialog_fragment, null);
        spinner = addDialogFragmentView.findViewById(R.id.add_spinner);
        nameEditText = addDialogFragmentView.findViewById(R.id.stop_name_editText);
        nameEditText.addTextChangedListener(textWatcher);


        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.options,
                android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    nameEditText.setHint(getString(R.string.stop));
                    tramStop = true;
                } else {
                    nameEditText.setHint(R.string.bike_station);
                    tramStop = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(addDialogFragmentView);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (tramStop) {
                    Stop stop = DataGetter.getStopByName(nameEditText.getText().toString());
                    String symbols = "";
                    for (int i = 0; i < stop.getCount(); i++) {
                        symbols += stop.getSymbol(i) + ", ";
                    }
                    addStop(stop);
                } else {
                    addBikesStation(nameEditText.getText().toString());
                }
                dataBaseRefreshInterface.refreshData();


            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddDialogFragment.this.getDialog().cancel();
            }
        });
        dialog = builder.create();
        return dialog;
    }

    private void addStop(Stop stop) {
        boolean answer = stopsDataBaseHelper.addStop(stop);
        if (answer) {
            Toast.makeText(getContext(),
                    getString(R.string.successful_adding_stop) + stop.getName(),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.error_by_adding_stop),
                    Toast.LENGTH_LONG).show();
        }
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

    public void setPlaces(JSONArray places) {
        this.places = places;
    }

    public void setBikesDataBaseHelper(BikesDataBaseHelper bikesDataBaseHelper) {
        this.bikesDataBaseHelper = bikesDataBaseHelper;
    }

    public void setStopsDataBaseHelper(StopsDataBaseHelper stopsDataBaseHelper) {
        this.stopsDataBaseHelper = stopsDataBaseHelper;
    }

    private boolean contains(ArrayList<String> names, String string) {
        for (String name : names) {
            if (name.compareTo(string) == 0) {
                return true;
            }
        }
        return false;
    }

    public void setDataBaseRefreshInterface(DataBaseRefreshInterface dataBaseRefreshInterface) {
        this.dataBaseRefreshInterface = dataBaseRefreshInterface;
    }
}
