package com.kuralev.departures.model;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;

import com.kuralev.departures.R;
import com.kuralev.departures.locationprovider.FuseLocationProvider;
import com.kuralev.departures.locationprovider.Location;
import com.kuralev.departures.locationprovider.LocationProvider;
import com.kuralev.departures.model.dataloaders.DeparturesLoaderCallback;
import com.kuralev.departures.model.dataloaders.StationsLoaderCallback;
import com.kuralev.departures.model.departure.Departure;
import com.kuralev.departures.model.station.Station;
import com.kuralev.departures.view.MainView;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private MainView mainView;
    private AppCompatActivity activity;
    private List<Station> nearbyStations = new ArrayList<>();
    private List<Departure> departureList = new ArrayList<>();
    private final static int STATIONS_LOADER_ID = 0;
    private final static int DEPARTURES_LOADER_ID = 1;
    private LocationProvider locationProvider;
    private Location deviceLocation;

    public Model(AppCompatActivity activity, MainView mainView) {
        this.mainView = mainView;
        this.activity = activity;
        this.locationProvider = new FuseLocationProvider(activity);
    }

    public void updateDeparturesList() {
        if (deviceLocation != null)
            updateDeparturesList(deviceLocation);
        else
            updateLocation();
    }

    public void updateDeparturesList(Station station) {
        mainView.dataUpdateStarted();
        LoaderManager.getInstance(activity).restartLoader(DEPARTURES_LOADER_ID, null, new DeparturesLoaderCallback(activity, station, this));
        //successful update will end up in departuresListUpdated call
    }

    private void updateDeparturesList(Location location) {
        mainView.dataUpdateStarted();
        //we first need to get stations list for the location
        LoaderManager.getInstance(activity).restartLoader(STATIONS_LOADER_ID, null, new StationsLoaderCallback(activity, location, this));
        //successful update will end up in nearbyStationsUpdated call
    }

    public void updateLocation() {
        if (checkLocationPermissions()) {
            setOnLocationUpdateListener();
            mainView.dataUpdateStarted();
            locationProvider.requestLocationUpdate();
        }
        else
            requestLocationPermissions();
    }

    public List<Station> getNearbyStations() {
        return nearbyStations;
    }

    private boolean checkLocationPermissions() {
        return (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void setOnLocationUpdateListener() {
        locationProvider.setOnLocationUpdatedListener(new LocationProvider.OnLocationUpdated() {
            @Override
            public void locationUpdated(Location location) {
                if (location != null) {
                    deviceLocation = location;
                    updateDeparturesList();
                }
                else {
                    mainView.operationFailed(R.string.location_data_not_available);
                    mainView.dataUpdateFinished();
                }
            }
        });
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    public void nearbyStationsUpdated(List<Station> nearbyStations) {
        if (nearbyStations != null && nearbyStations.size() > 0) {
            this.nearbyStations = nearbyStations;
            Station closestStation = nearbyStations.get(0);
            updateDeparturesList(closestStation);
            mainView.currentStationChanged(closestStation);
        }
    }

    public void departuresListUpdated(List<Departure> departureList) {
        this.departureList = departureList;
        if (departureList != null && departureList.size() > 0)
            mainView.departureListUpdated(departureList);
        mainView.dataUpdateFinished();
    }
}
