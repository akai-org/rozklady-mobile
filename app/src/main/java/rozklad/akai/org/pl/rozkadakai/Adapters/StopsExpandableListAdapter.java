package rozklad.akai.org.pl.rozkadakai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import rozklad.akai.org.pl.rozkadakai.Data.Stop;
import rozklad.akai.org.pl.rozkadakai.R;

public class StopsExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<Stop> stops;
    private Context context;

    public StopsExpandableListAdapter(ArrayList<Stop> stops, Context context) {
        this.stops = stops;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return stops.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return stops.get(groupPosition).getCount();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return stops.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return stops.get(groupPosition).getSymbol(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Stop stop = stops.get(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.stops_list_group, null);
        }
        TextView nameTextView = convertView.findViewById(R.id.stop_name_list_textView);
        nameTextView.setText(stop.getName());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Stop stop = stops.get(groupPosition);
        String name = stop.getSymbol(childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.stops_list_item, null);
        }
        TextView txtListChild = convertView
                .findViewById(R.id.stop_symbol_list_textView);

        final CheckBox checkBox = convertView.findViewById(R.id.item_check_box);
        checkBox.setChecked(stop.getBoolean(childPosition));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stops.get(groupPosition).setBoolean(childPosition, !checkBox.isChecked());
                checkBox.setChecked(!checkBox.isChecked());
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stops.get(groupPosition).setBoolean(childPosition, isChecked);
                //checkBox.setChecked(!checkBox.isChecked());
            }
        });

        txtListChild.setText(name);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public ArrayList<Stop> getStops() {
        return stops;
    }
}
