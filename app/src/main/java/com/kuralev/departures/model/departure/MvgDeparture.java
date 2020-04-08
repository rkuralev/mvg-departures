package com.kuralev.departures.model.departure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MvgDeparture implements Departure {
    private String lineColor;
    private String lineNumber;
    private String lineDirection;
    private Long departureTime;

    public MvgDeparture(
            @JsonProperty("lineBackgroundColor") String lineColor,
            @JsonProperty("label") String lineNumber,
            @JsonProperty("destination") String lineDirection,
            @JsonProperty("departureTime") Long departureTime) {
        this.lineColor = lineColor;
        this.lineNumber = lineNumber;
        this.lineDirection = lineDirection;
        this.departureTime = departureTime;
        int a = 0;
    }

    @Override
    public String getLineColor() {
        return lineColor;
    }

    @Override
    public String getLineNumber() {
        return lineNumber;
    }

    @Override
    public String getLineDirection() {
        return lineDirection;
    }

    @Override
    public long getDepartureTime() {
        return departureTime;
    }
}
