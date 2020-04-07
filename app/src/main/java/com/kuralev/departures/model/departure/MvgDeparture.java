package com.kuralev.departures.model.departure;

import android.graphics.Color;

public class MvgDeparture implements Departure {
    private String lineColor;
    private String lineNumber;
    private String lineDirection;
    private Long departureTime;

    public MvgDeparture(String lineNumber, String lineColor, String lineDirection, long departureTime) {
        this.lineNumber = lineNumber;
        this.lineColor = lineColor;
        this.lineDirection = lineDirection;
        this.departureTime = departureTime;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public String getLineDirection() {
        return lineDirection;
    }

    public int getLineColor() {
        return Color.parseColor(lineColor);
    }

    public String getTimeToDeparture() {
        long now = System.currentTimeMillis() / 1000;
        int timeToDeparture = (int) (departureTime - now) / 60;
        int result = Math.max(timeToDeparture, 0);
        return Integer.toString(result);
    }

}
