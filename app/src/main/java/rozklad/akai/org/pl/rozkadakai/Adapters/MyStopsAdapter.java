package rozklad.akai.org.pl.rozkadakai.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Data.Stop;
import rozklad.akai.org.pl.rozkadakai.R;

public class MyStopsAdapter extends RecyclerView.Adapter<MyStopsAdapter.MyStopsViewHolder> {

    private ArrayList<Stop> stops;

    public MyStopsAdapter(ArrayList<Stop> stops) {
        this.stops = stops;
    }

    @NonNull
    @Override
    public MyStopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.my_stop_row, parent, false);
        return new MyStopsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyStopsViewHolder myStopsViewHolder, int i) {
        Stop stop = stops.get(i);
        myStopsViewHolder.nameTextView.setText(stop.getName());
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    public void setStops(ArrayList<Stop> stops) {
        this.stops = stops;
    }

    class MyStopsViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;

        public MyStopsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.stop_name_row_textView);
        }
    }
}
