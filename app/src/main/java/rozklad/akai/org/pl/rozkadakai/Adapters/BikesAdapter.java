package rozklad.akai.org.pl.rozkadakai.Adapters;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Data.Place;
import rozklad.akai.org.pl.rozkadakai.MainActivity;
import rozklad.akai.org.pl.rozkadakai.R;

public class BikesAdapter extends RecyclerView.Adapter<BikesAdapter.BikesViewHolder> {

    private ArrayList<Place> places = null;
    private MainActivity parentActivity = null;

    public BikesAdapter(ArrayList<Place> places, MainActivity parentActivity) {
        this.places = places;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public BikesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.bikes_row, parent, false);
        return new BikesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BikesViewHolder bikesViewHolder, int i) {
        Place place = places.get(i);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (place.getCount() <= 2) {
                bikesViewHolder.countTextView.setTextColor(
                        parentActivity.getApplicationContext().getColor(R.color.red));
            } else if (place.getCount() < 5) {
                bikesViewHolder.countTextView.setTextColor(
                        parentActivity.getApplicationContext().getColor(R.color.orange));

            } else {
                bikesViewHolder.countTextView.setTextColor(Color.GRAY);
            }
            bikesViewHolder.nameTextView.setTextColor(Color.GRAY);
        }
        bikesViewHolder.nameTextView.setText(place.getName());
        bikesViewHolder.countTextView.setText(String.valueOf(place.getCount()));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    class BikesViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView countTextView;

        public BikesViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.bike_name_text_view);
            countTextView = itemView.findViewById(R.id.bike_count_text_view);
        }


    }

}
