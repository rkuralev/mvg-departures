package com.kuralev.departures.model.departure;

public interface Departure {

    String getLineNumber();
    String getLineColor();
    String getLineDirection();
    long getDepartureTime();

}
