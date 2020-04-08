package com.kuralev.departures.model.departure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MvgDeparturesList {
    @JsonDeserialize(as = ArrayList.class, contentAs = MvgDeparture.class)
    @JsonProperty("departures")
    private List<Departure> departureList;

    @JsonCreator
    public MvgDeparturesList(@JsonProperty("departures") List<Departure> departureList) {
        this.departureList = departureList;
    }

    public List<Departure> getDepartureList() {
        return departureList;
    }
}
