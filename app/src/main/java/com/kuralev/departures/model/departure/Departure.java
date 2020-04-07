package com.kuralev.departures.model.departure;

public interface Departure {

    String getLineNumber();
    int getLineColor();
    String getLineDirection();
    String getTimeToDeparture();

}
