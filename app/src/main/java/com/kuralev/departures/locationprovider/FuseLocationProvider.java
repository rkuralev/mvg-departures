package com.kuralev.departures.locationprovider;

import android.app.Activity;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class FuseLocationProvider extends LocationProvider {

    private Activity mActivity;

    public FuseLocationProvider(Activity activity) {
        this.mActivity = activity;
    }

    private void initializeLocationUpdate() {
        final LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (android.location.Location l : locationResult.getLocations())
                        getOnLocationUpdatedListener().locationUpdated(new Location(l.getLatitude(), l.getLongitude()));
                }
            }
        };

        final FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);

        LocationRequest request = LocationRequest.create();
        request.setInterval(10000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());

    }

    @Override
    public void requestLocationUpdate() {
        initializeLocationUpdate();
    }

}