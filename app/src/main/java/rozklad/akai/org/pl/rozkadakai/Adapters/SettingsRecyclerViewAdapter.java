package rozklad.akai.org.pl.rozkadakai.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Activities.MainActivity;
import rozklad.akai.org.pl.rozkadakai.Fragments.SettingsFragment;
import rozklad.akai.org.pl.rozkadakai.R;

import static rozklad.akai.org.pl.rozkadakai.Constants.KOSSA_LOG;

public class SettingsRecyclerViewAdapter extends RecyclerView.Adapter<SettingsRecyclerViewAdapter.SettingsViewHolder> {

    private ArrayList<String> settings = new ArrayList<>();
    private MainActivity parent = null;
    private SettingsFragment parentFragment;
    private boolean showPut;

    public SettingsRecyclerViewAdapter(MainActivity parent, SettingsFragment parentFragment, boolean showPut) {
        this.parent = parent;
        this.parentFragment = parentFragment;
        this.showPut = showPut;
        settings.add("Manage Stops");
        settings.add("Manage Bike Places");
        settings.add("Manage Put");
    }

    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        SettingsViewHolder holder;
        view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.settings_row, viewGroup, false);
        holder = new SettingsViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SettingsViewHolder settingsViewHolder, int i) {
        if (settings.get(i).compareTo("Manage Stops") == 0) {
            settingsViewHolder.textView.setText(parent.getString(R.string.manage_stops));
            settingsViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentFragment.openTramsSettings();
                }
            });
        } else if (settings.get(i).compareTo("Manage Bike Places") == 0) {
            settingsViewHolder.textView.setText(parent.getString(R.string.manage_bikes));
            settingsViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentFragment.openBikesSettings();
                }
            });
        } else if (settings.get(i).compareTo("Manage Put") == 0) {
            Log.d(KOSSA_LOG, "With switch: " + settingsViewHolder.withSwitch + " i = " + i);
            settingsViewHolder.aSwitch.setChecked(showPut);
            settingsViewHolder.aSwitch.setVisibility(View.VISIBLE);
            settingsViewHolder.textView.setText(parent.getString(R.string.manage_put));
            settingsViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settingsViewHolder.aSwitch.setChecked(!settingsViewHolder.aSwitch.isChecked());
                    parent.saveChecked(settingsViewHolder.aSwitch.isChecked());
                    showPut = settingsViewHolder.aSwitch.isChecked();

                }
            });
            settingsViewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    parent.saveChecked(isChecked);
                    showPut = settingsViewHolder.aSwitch.isChecked();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder {

        private boolean withSwitch;
        private TextView textView;
        private Switch aSwitch = null;

        public SettingsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.settings_row_textView);
            this.withSwitch = withSwitch;
            aSwitch = itemView.findViewById(R.id.settings_row_switch);

        }

    }
}
