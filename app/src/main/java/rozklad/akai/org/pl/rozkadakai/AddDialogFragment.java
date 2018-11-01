package rozklad.akai.org.pl.rozkadakai;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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

public class AddDialogFragment extends DialogFragment {

    private AlertDialog dialog;
    private View addDialogFragmentView;
    private AutoCompleteTextView nameEditText;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private boolean tramStop = true;
    private JSONArray places;
    private TextInputLayout floatingLabel;
    private BikesDataBaseHelper bikesDataBaseHelper;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() >= 2) {
                if (tramStop) {
                    Toast.makeText(getContext(), "Text: " + s, Toast.LENGTH_SHORT).show();
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

            } else {
                ArrayList<String> names = DataGetter.getPlacesNamesByPattern(s.toString(), places);
                if (names.size() == 1) {
                    if (s.toString().compareTo(names.get(0)) == 0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    } else {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        }
    };

    public AddDialogFragment() {
        super();
    }

    public static AddDialogFragment newInstance(JSONArray places, BikesDataBaseHelper bikesDataBaseHelper) {
        AddDialogFragment addDialogFragment = new AddDialogFragment();
        addDialogFragment.setPlaces(places);
        addDialogFragment.setBikesDataBaseHelper(bikesDataBaseHelper);
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
        floatingLabel = addDialogFragmentView.findViewById(R.id.tram_input_layout);
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
                    Toast.makeText(getContext(), "Add", Toast.LENGTH_SHORT).show();
                } else {
                    addBikesStation(nameEditText.getText().toString());
                }


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

    private void addBikesStation(String name) {
        boolean anwser = bikesDataBaseHelper.addStationName(name);
        if (anwser) {
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
}
