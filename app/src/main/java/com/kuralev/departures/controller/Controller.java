package com.kuralev.departures.controller;

import android.content.Context;

import com.kuralev.departures.model.Model;
import com.kuralev.departures.model.station.Station;
import com.kuralev.departures.view.DeparturesListAdapter;

import java.util.List;

public class Controller {
    private Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public DeparturesListAdapter getDeparturesListAdapter(Context context) {
        return new DeparturesListAdapter(context);
    }

    public void updateLocation() {
        model.updateLocation();
    }

    public void updateDeparturesList() {
        model.updateDeparturesList();
    }

    public void updateDeparturesList(Station chosenStation) {
        model.updateDeparturesList(chosenStation);
    }

    public List<Station> getNearbyStations() {
        return model.getNearbyStations();
    }

}
