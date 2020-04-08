package com.kuralev.departures.model.dataloaders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.kuralev.departures.model.dataprovider.DepartureProvider;
import com.kuralev.departures.model.dataprovider.MvgApiProvider;
import com.kuralev.departures.model.departure.Departure;
import com.kuralev.departures.model.station.Station;

import java.util.List;

public class DeparturesLoader extends AsyncTaskLoader<List<Departure>> {

    private Station station;

    public DeparturesLoader(@NonNull Context context, Station station) {
        super(context);
        this.station = station;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Departure> loadInBackground() {
        DepartureProvider departureProvider = MvgApiProvider.getInstance();
        return departureProvider.fetchDepartures(station);
    }
}
