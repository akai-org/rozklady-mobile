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

import java.util.ArrayList;
import java.util.Objects;

import rozklad.akai.org.pl.rozkadakai.Data.Stop;
import rozklad.akai.org.pl.rozkadakai.DataBaseHelpers.StopsDataBaseHelper;
import rozklad.akai.org.pl.rozkadakai.DataGetter;
import rozklad.akai.org.pl.rozkadakai.R;
import rozklad.akai.org.pl.rozkadakai.RefreshInterface;

public class TramAddDialogFragment extends DialogFragment {

    private View dialogView;
    private AlertDialog dialog;
    private RefreshInterface refreshInterface;
    private StopsDataBaseHelper stopsDataBaseHelper;
    private AutoCompleteTextView nameEditText;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() % 3 == 2) {
                ArrayList<String> names = DataGetter.getStopsByPattern(s.toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
                nameEditText.setAdapter(adapter);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            ArrayList<String> names = DataGetter.getStopsByPattern(s.toString());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
            nameEditText.setAdapter(adapter);

            boolean cont = contains(names, s.toString());
            if (cont) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            } else {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        }
    };

    public static TramAddDialogFragment newInstance(StopsDataBaseHelper stopsDataBaseHelper,
                                                    RefreshInterface refreshInterface) {
        TramAddDialogFragment tramAddDialogFragment = new TramAddDialogFragment();
        tramAddDialogFragment.setRefreshInterface(refreshInterface);
        tramAddDialogFragment.setStopsDataBaseHelper(stopsDataBaseHelper);

        return tramAddDialogFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();

        dialogView = inflater.inflate(R.layout.stop_add_dialog_fragment, null);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Stop stop = DataGetter.getStopByName(nameEditText.getText().toString());
                String symbols = "";
                for (int i = 0; i < stop.getCount(); i++) {
                    symbols += stop.getSymbol(i) + ", ";
                }
                addStop(stop);

                refreshInterface.refreshData();
                refreshInterface.refreshView();
                refreshInterface.openView("trams");

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TramAddDialogFragment.this.getDialog().cancel();
                refreshInterface.refreshView();
            }
        });
        nameEditText = dialogView.findViewById(R.id.name_editText);
        nameEditText.addTextChangedListener(textWatcher);
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

    public void setRefreshInterface(RefreshInterface refreshInterface) {
        this.refreshInterface = refreshInterface;
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
}
