package com.kuralev.departures.model.station;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MvgStation implements Station {
    private String stationName;
    private String stationId;
    private String distanceToStation;

    @JsonCreator
    public MvgStation(
            @JsonProperty("name") String stationName,
            @JsonProperty("id") String stationId,
            @JsonProperty("distance") String distanceToStation) {
        this.stationName = stationName;
        this.stationId = stationId;
        this.distanceToStation = distanceToStation;
    }

    @Override
    public String getStationName() {
        return stationName;
    }

    @Override
    public String getDistanceToStation() {
        return distanceToStation;
    }

    @Override
    public String getStationId() {
        return stationId;
    }
}
