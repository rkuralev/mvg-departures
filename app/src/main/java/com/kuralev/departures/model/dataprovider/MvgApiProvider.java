package com.kuralev.departures.model.dataprovider;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuralev.departures.locationprovider.Location;
import com.kuralev.departures.model.departure.Departure;
import com.kuralev.departures.model.departure.MvgDeparturesList;
import com.kuralev.departures.model.station.MvgStationsList;
import com.kuralev.departures.model.station.Station;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

        MvgDeparturesList departuresList =  retrieveDataFromApi(requestUrl, MvgDeparturesList.class);

        return (departuresList == null) ? null : departuresList.getDepartureList();
    }

    @Override
    public List<Station> fetchNearbyStations(Location location) {
        if (location == null)
            return new ArrayList<>();

        String requestUrl = NEARBY_URL
                .replace("{lat}", location.getLatitude())
                .replace("{lon}", location.getLongitude());

        MvgStationsList stationsList = retrieveDataFromApi(requestUrl, MvgStationsList.class);

        return (stationsList == null) ? null : stationsList.getStationList();
    }

    /** Makes an API request and returns an object of a corresponding class
      executes GET request and parses the result using Jackson */

    private <T> T retrieveDataFromApi(String url, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        OkHttpClient client = new OkHttpClient();
        T result = null;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept","application/json")
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("X-MVG-Authorization-Key", AUTH_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                result = mapper.readValue(jsonResponse, clazz);
            }
            else
                Log.e(LOG_TAG,  String.format("API error. Response code: %d", response.code()));
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return result;
    }
}
