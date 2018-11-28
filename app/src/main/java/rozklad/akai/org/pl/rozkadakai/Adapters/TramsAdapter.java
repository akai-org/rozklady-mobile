package rozklad.akai.org.pl.rozkadakai.Adapters;

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

import rozklad.akai.org.pl.rozkadakai.Activities.MainActivity;
import rozklad.akai.org.pl.rozkadakai.Data.Tram;
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
            if (min >= 0) {
                tramsViewHolder.departureTextView.setText("< " + min + "min");
            } else {
                tramsViewHolder.departureTextView.setText(parentActivity.getString(R.string.missed) + " " + -min + "min temu");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (min < 0) {
                    tramsViewHolder.departureTextView.setTextColor(
                            parentActivity.getApplicationContext().getColor(R.color.red));
                }
                if (min == 0) {
                    tramsViewHolder.departureTextView.setText(R.string.leaves_now);
                }
                if (min <= 2) {
                    tramsViewHolder.departureTextView.setTextColor(
                            parentActivity.getApplicationContext().getColor(R.color.red));
                } else if (min < 5) {

                    tramsViewHolder.departureTextView.setTextColor(
                            parentActivity.getApplicationContext().getColor(R.color.orange));
                } else {
                    tramsViewHolder.departureTextView.setTextColor(
                            parentActivity.getApplicationContext().getColor(R.color.black_overlay));
                }
            }
        } else {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            date.setTime(departure.getTime() - 3600000);
            tramsViewHolder.departureTextView.setText(format.format(date));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tramsViewHolder.departureTextView.setTextColor(parentActivity.getApplicationContext().getColor(R.color.black_overlay));
            }
        }
        tramsViewHolder.lineTextView.setText(tram.getLine());
    }

    @Override
    public int getItemCount() {
        if (trams == null) {
            return 0;
        }
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
