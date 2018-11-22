package rozklad.akai.org.pl.rozkadakai.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Activities.BikesActivity;
import rozklad.akai.org.pl.rozkadakai.Data.Place;
import rozklad.akai.org.pl.rozkadakai.R;

public class BikesSettingsAdapter extends RecyclerView.Adapter<BikesSettingsAdapter.BikeSettingsViewHolder> {

    private ArrayList<Place> places;
    private BikesActivity parentActivity;

    public BikesSettingsAdapter(ArrayList<Place> places, BikesActivity parentActivity) {
        this.places = places;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public BikeSettingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.bikes_settings_row, viewGroup, false);
        return new BikeSettingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BikeSettingsViewHolder bikeSettingsViewHolder, final int i) {
        final Place place = places.get(i);
        bikeSettingsViewHolder.nameTextView.setText(place.getName());
        bikeSettingsViewHolder.checkBox.setChecked(place.isShow());
        bikeSettingsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bikeSettingsViewHolder.checkBox.setChecked(!bikeSettingsViewHolder.checkBox.isChecked());
                places.get(i).setShow(!bikeSettingsViewHolder.checkBox.isChecked());
                parentActivity.saveStations();
            }
        });
        bikeSettingsViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                places.get(i).setShow(isChecked);
                parentActivity.saveStations();
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    protected class BikeSettingsViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView nameTextView;
        private CheckBox checkBox;

        public BikeSettingsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.checkBox = itemView.findViewById(R.id.bike_station_checkBox);
            this.nameTextView = itemView.findViewById(R.id.bike_station_name_textView);
        }
    }
}
