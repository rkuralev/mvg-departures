package com.kuralev.departures.locationprovider;

public abstract class LocationProvider {
    public abstract void requestLocationUpdate();

    public interface OnLocationUpdated {
        void locationUpdated(Location location);
    }

    private OnLocationUpdated onLocationUpdatedListener;

    OnLocationUpdated getOnLocationUpdatedListener() {
        return onLocationUpdatedListener;
    }

    public void setOnLocationUpdatedListener(OnLocationUpdated listener) {
        this.onLocationUpdatedListener = listener;
    }
}
