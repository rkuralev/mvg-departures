package com.kuralev.departures.view;

import com.kuralev.departures.model.departure.Departure;
import com.kuralev.departures.model.station.Station;

import java.util.List;

public interface MainView {
    void operationFailed(int messageResourceId);
    void currentStationChanged(Station station);
    void departureListUpdated(List<Departure> departures);
    void dataUpdateStarted();
    void dataUpdateFinished();
}
