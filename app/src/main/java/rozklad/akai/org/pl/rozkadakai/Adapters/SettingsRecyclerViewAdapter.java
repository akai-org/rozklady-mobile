package rozklad.akai.org.pl.rozkadakai.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Activities.MainActivity;
import rozklad.akai.org.pl.rozkadakai.Fragments.SettingsFragment;
import rozklad.akai.org.pl.rozkadakai.R;

public class SettingsRecyclerViewAdapter extends RecyclerView.Adapter<SettingsRecyclerViewAdapter.SettingsViewHolder> {

    private ArrayList<String> settings = new ArrayList<>();
    private MainActivity parent = null;
    private SettingsFragment parentFragment;

    public SettingsRecyclerViewAdapter(MainActivity parent, SettingsFragment parentFragment) {
        this.parent = parent;
        this.parentFragment = parentFragment;
        settings.add("Manage Stops");
        settings.add("Manage Bike Places");
    }

    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.settings_row, viewGroup, false);
        SettingsViewHolder holder = new SettingsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder settingsViewHolder, int i) {
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
        }
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public SettingsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.settings_row_textView);
        }
    }
}
