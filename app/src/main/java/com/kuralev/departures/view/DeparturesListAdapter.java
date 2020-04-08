package com.kuralev.departures.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kuralev.departures.R;
import com.kuralev.departures.model.departure.Departure;

public class DeparturesListAdapter extends ArrayAdapter<Departure> {

    public DeparturesListAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        Departure departure = getItem(position);

        TextView lineNumber = listItemView.findViewById(R.id.line_number);
        TextView lineDirection = listItemView.findViewById(R.id.line_direction);
        TextView timeToDeparture = listItemView.findViewById(R.id.time_to_departure);

        lineNumber.setText(departure.getLineNumber());
        lineNumber.setBackgroundColor(Color.parseColor(departure.getLineColor())); //color is in hex #0d5c70
        lineDirection.setText(departure.getLineDirection());
        timeToDeparture.setText(calculateTimeToDeparture(departure.getDepartureTime()));

        return listItemView;
    }

    private String calculateTimeToDeparture(long departureTimeInMs) {
        //departureTime is in milliseconds (UNIX epoch)
        long now = System.currentTimeMillis();
        int timeToDeparture = (int) (departureTimeInMs - now) / 1000 / 60;
        return String.valueOf(Math.max(timeToDeparture, 0));
    }
}
