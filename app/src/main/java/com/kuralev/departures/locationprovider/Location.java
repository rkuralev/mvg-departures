package com.kuralev.departures.locationprovider;

import androidx.annotation.Nullable;

public class Location {
    private String latitude;
    private String longitude;

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude.toString();
        this.longitude = longitude.toString();
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (o instanceof Location) {
            Location l = (Location) o;
            return (this.latitude.equals(l.getLatitude()) && this.longitude.equals(l.getLongitude()));
        }
        else return false;
    }
}
