package com.kuralev.departures.model.dataloaders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.kuralev.departures.model.Model;
import com.kuralev.departures.model.departure.Departure;
import com.kuralev.departures.model.station.Station;

import java.util.List;

public class DeparturesLoaderCallback implements LoaderManager.LoaderCallbacks<List<Departure>> {

    private Station station;
    private Activity context;
    private Model model;

    public DeparturesLoaderCallback(Activity context, Station station, Model model) {
        this.station = station;
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public Loader<List<Departure>> onCreateLoader(int id, @Nullable Bundle args) {
        return new DeparturesLoader(context, station);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Departure>> loader, List<Departure> data) {
        model.departuresListUpdated(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Departure>> loader) {
    }

}
