package rozklad.akai.org.pl.rozkadakai.Adapters;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rozklad.akai.org.pl.rozkadakai.Data.Tram;
import rozklad.akai.org.pl.rozkadakai.MainActivity;
import rozklad.akai.org.pl.rozkadakai.R;

public class TramsAdapter extends RecyclerView.Adapter<TramsAdapter.TramsViewHolder> {

    private ArrayList<Tram> trams;
    private MainActivity parentActivity;
    private String symbol;

    public TramsAdapter(ArrayList<Tram> trams, MainActivity parentActivity, String symbol) {
        this.trams = trams;
        this.parentActivity = parentActivity;
        this.symbol = symbol;
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
        Date departure = tram.getDepartureDate();
        if (tram.isRealTime()) {
            long div = departure.getTime() - new Date().getTime();
            long min = div / 60000 - 60;
            tramsViewHolder.departureTextView.setText("< " + min + "min");
            //Log.d(KOSSA_LOG, "min = " + min + " dep = " + tram.getDeparture());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (min <= 2) {
                    tramsViewHolder.departureTextView.setTextColor(
                            parentActivity.getApplicationContext().getColor(R.color.red));
                } else if (min < 5) {

                    tramsViewHolder.departureTextView.setTextColor(
                            parentActivity.getApplicationContext().getColor(R.color.orange));
                } else {
                    tramsViewHolder.departureTextView.setTextColor(Color.GRAY);
                }
            }
        } else {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            departure.setTime(departure.getTime() - 3600000);
            tramsViewHolder.departureTextView.setText(format.format(tram.getDepartureDate()));
            tramsViewHolder.departureTextView.setTextColor(Color.GRAY);
        }
        tramsViewHolder.lineTextView.setText(tram.getLine());
    }

    @Override
    public int getItemCount() {
        return trams.size();
    }

    public void setTrams(ArrayList<Tram> trams) {
        this.trams = trams;
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
