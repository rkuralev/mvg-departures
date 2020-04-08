package com.kuralev.departures.model.dataprovider;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuralev.departures.locationprovider.Location;
import com.kuralev.departures.model.departure.Departure;
import com.kuralev.departures.model.departure.MvgDeparturesList;
import com.kuralev.departures.model.station.MvgStationsList;
import com.kuralev.departures.model.station.Station;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MvgApiProvider implements DepartureProvider {

    private final static String DEPARTURES_URL = "https://www.mvg.de/api/fahrinfo/departure/{id}?footway=0";
    private final static String NEARBY_URL = "https://www.mvg.de/api/fahrinfo/location/nearby?latitude={lat}&longitude={lon}";
    private final static String AUTH_KEY = "5af1beca494712ed38d313714d4caff6";
    private final static String USER_AGENT = "java-mvg-api/1";
    private final static String LOG_TAG = "MVGAPIProvider";
    private static MvgApiProvider instance;

    private MvgApiProvider() {}

    public static MvgApiProvider getInstance() {
        if (instance == null)
            instance = new MvgApiProvider();
        return instance;
    }

    @Override
    public List<Departure> fetchDepartures(Station station) {
        if (station == null)
            return new ArrayList<>();

        String requestUrl = DEPARTURES_URL
                .replace("{id}", station.getStationId());

        return retrieveDataFromApi(requestUrl, MvgDeparturesList.class).getDepartureList();
    }

    @Override
    public List<Station> fetchNearbyStations(Location location) {
        if (location == null)
            return new ArrayList<>();

        String requestUrl = NEARBY_URL
                .replace("{lat}", location.getLatitude())
                .replace("{lon}", location.getLongitude());

        return retrieveDataFromApi(requestUrl, MvgStationsList.class).getStationList();
    }

    /** Makes an API request and returns an object of a corresponding class
      executes GET request and parses the result using Jackson */

    private <T> T retrieveDataFromApi(String url, Class<T> clazz) {
        InputStream is = null;
        HttpURLConnection connection = null;
        T result = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("X-MVG-Authorization-Key", AUTH_KEY);
            is = connection.getInputStream();
            result = mapper.readValue(is, clazz);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }
}
