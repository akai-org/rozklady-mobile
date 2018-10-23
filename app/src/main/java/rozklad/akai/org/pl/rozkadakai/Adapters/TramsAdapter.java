package rozklad.akai.org.pl.rozkadakai.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Data.Tram;
import rozklad.akai.org.pl.rozkadakai.MainActivity;
import rozklad.akai.org.pl.rozkadakai.R;

public class TramsAdapter extends RecyclerView.Adapter<TramsAdapter.TramsViewHolder> {

    private ArrayList<Tram> trams;
    private MainActivity parentActivity;

    public TramsAdapter(ArrayList<Tram> trams, MainActivity parentActivity) {
        this.trams = trams;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public TramsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.tram_row, parent, false);
        return new TramsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TramsViewHolder tramsViewHolder, int i) {
        Tram tram = trams.get(i);
        tramsViewHolder.directionTextView.setText(tram.getDirection());
        tramsViewHolder.departureTextView.setText(tram.getDeparture());
        tramsViewHolder.lineTextView.setText(tram.getLine());
    }

    @Override
    public int getItemCount() {
        return trams.size();
    }

    public class TramsViewHolder extends RecyclerView.ViewHolder {

        private TextView lineTextView;
        private TextView departureTextView;
        private TextView directionTextView;

        public TramsViewHolder(@NonNull View itemView) {
            super(itemView);
            lineTextView = itemView.findViewById(R.id.line_text_view);
            departureTextView = itemView.findViewById(R.id.departure_text_view);
            directionTextView = itemView.findViewById(R.id.direction_text_view);
        }
    }
}
