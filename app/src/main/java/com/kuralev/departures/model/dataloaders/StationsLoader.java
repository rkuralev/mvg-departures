package com.kuralev.departures.model.dataloaders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.kuralev.departures.locationprovider.Location;
import com.kuralev.departures.model.dataprovider.DepartureProvider;
import com.kuralev.departures.model.dataprovider.MvgApiProvider;
import com.kuralev.departures.model.station.Station;

import java.util.List;

public class StationsLoader extends AsyncTaskLoader<List<Station>> {

    private Location location;

    public StationsLoader(@NonNull Context context, Location location) {
        super(context);
        this.location = location;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Station> loadInBackground() {
        DepartureProvider departureProvider = MvgApiProvider.getInstance();
        return departureProvider.getNearbyStations(location);
    }
}
