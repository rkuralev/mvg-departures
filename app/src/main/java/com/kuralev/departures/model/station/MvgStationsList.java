package com.kuralev.departures.model.station;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MvgStationsList {

    @JsonDeserialize(as = ArrayList.class, contentAs = MvgStation.class)
    @JsonProperty("locations")
    private List<Station> stationList;

    @JsonCreator
    public MvgStationsList(@JsonProperty("locations") List<Station> stationList) {
        this.stationList = stationList;
    }

    public List<Station> getStationList() {
        return stationList;
    }
}
