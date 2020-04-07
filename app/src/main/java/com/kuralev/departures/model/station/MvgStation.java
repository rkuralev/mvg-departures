package com.kuralev.departures.model.station;

public class MvgStation implements Station {

    private String stationName;
    private String stationId;
    private String distanceToStation;

    public MvgStation(String stationName, String stationId, String distanceToStation) {
        this.stationName = stationName;
        this.stationId = stationId;
        this.distanceToStation = distanceToStation;
    }

    @Override
    public String getStationName() {
        String result = stationName;
        if (!distanceToStation.isEmpty())
            result += String.format(" (%s m)", distanceToStation);
        return result;
    }

    @Override
    public String getStationId() {
        return stationId;
    }
}
