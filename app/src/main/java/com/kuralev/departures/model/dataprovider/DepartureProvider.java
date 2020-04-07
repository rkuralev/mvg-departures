package com.kuralev.departures.model.dataprovider;

import com.kuralev.departures.locationprovider.Location;
import com.kuralev.departures.model.departure.Departure;
import com.kuralev.departures.model.station.Station;

import java.util.List;

public interface DepartureProvider {
    List<Departure> getDepartures(Station station);
    List<Station> getNearbyStations(Location location);
}
