package com.kuralev.departures.model.dataloaders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.kuralev.departures.locationprovider.Location;
import com.kuralev.departures.model.Model;
import com.kuralev.departures.model.station.Station;

import java.util.List;

public class StationsLoaderCallback implements LoaderManager.LoaderCallbacks<List<Station>> {

    private Activity context;
    private Location location;
    private Model model;

    public StationsLoaderCallback(Activity context, Location location, Model model) {
        this.context = context;
        this.location = location;
        this.model = model;
    }

    @NonNull
    @Override
    public Loader<List<Station>> onCreateLoader(int id, @Nullable Bundle args) {
        return new StationsLoader(context, location);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Station>> loader, List<Station> data) {
        model.nearbyStationsUpdated(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Station>> loader) {
    }
}
