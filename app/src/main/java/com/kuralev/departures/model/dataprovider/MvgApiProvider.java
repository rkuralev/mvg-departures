package com.kuralev.departures.model.dataprovider;

import android.util.Log;

import com.kuralev.departures.locationprovider.Location;
import com.kuralev.departures.model.departure.Departure;
import com.kuralev.departures.model.departure.MvgDeparture;
import com.kuralev.departures.model.station.MvgStation;
import com.kuralev.departures.model.station.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MvgApiProvider implements DepartureProvider {

    private final static String DEPARTURES_URL = "https://www.mvg.de/api/fahrinfo/departure/{id}?footway=0";
    private final static String NEARBY_URL = "https://www.mvg.de/api/fahrinfo/location/nearby?latitude={lat}&longitude={lon}";
    private final static String AUTH_KEY = "5af1beca494712ed38d313714d4caff6";
    private final static String USER_AGENT = "java-mvg-api/1";
    private final static String LOG_TAG = "MVGAPIProvider";
    private final static int NUMBER_OF_NEARBY_STATIONS = 10;
    private List<Station> stations;
    private Location lastLocation = null;
    private static MvgApiProvider instance;

    private MvgApiProvider() {}

    public static MvgApiProvider getInstance() {
        if (instance == null)
            instance = new MvgApiProvider();
        return instance;
    }

    //TODO Roman: migrate to Jackson

    @Override
    public List<Departure> getDepartures(Station station) {
        List<Departure> departures  = new ArrayList<>();
        String JSONResponse = "";
        String requestUrl = DEPARTURES_URL.replace("{id}", station.getStationId());

        try {
            URL url = new URL(requestUrl);
            JSONResponse = makeApiRequest(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Unable to form correct URL :: " + requestUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to retrieve data from MVG :: " + e.getMessage());
        }

        try {

            JSONObject root = new JSONObject(JSONResponse);
            JSONArray departuresJSONArray = root.getJSONArray("departures");
            for (int i = 0; i < departuresJSONArray.length(); i++) {
                JSONObject dep = departuresJSONArray.getJSONObject(i);

                String lineNumber = dep.getString("label");
                String destination = dep.getString("destination");
                String bgColor = dep.getString("lineBackgroundColor");
                BigInteger departureTimeInMs = new BigInteger(dep.getString("departureTime"));
                BigInteger departureTimeInSeconds = departureTimeInMs.divide(new BigInteger("1000"));
                long departureTime = departureTimeInSeconds.longValue();

                departures.add(new MvgDeparture(lineNumber, bgColor, destination, departureTime));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Unable to parse location JSON :: " + JSONResponse);
        }

        return departures;
    }

    @Override
    public List<Station> getNearbyStations(Location location) {
        if (location.equals(lastLocation))
            return stations;

        lastLocation = location;
        stations = new ArrayList<>();
        String requestUrl = NEARBY_URL.replace("{lat}", location.getLatitude()).replace("{lon}", location.getLongitude());
        String JSONResponse = "";

        try {
            URL url = new URL(requestUrl);
            JSONResponse = makeApiRequest(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Unable to form correct URL :: " + requestUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to retrieve data from MVG :: " + e.getMessage());
        }

        try {
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray stationLocations = root.getJSONArray("locations");
            int maxNumberOfStations = Math.min(NUMBER_OF_NEARBY_STATIONS, stationLocations.length());
            for (int i = 0; i < maxNumberOfStations; i++) {
                JSONObject st = stationLocations.getJSONObject(i);
                String stationName = st.getString("name");
                String stationId = st.getString("id");
                String distanceToStation = st.getString("distance");
                stations.add(new MvgStation(stationName, stationId, distanceToStation));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Unable to parse location JSON :: " + JSONResponse);
        }

        return stations;
    }

    private String makeApiRequest(URL url) throws IOException {
        HttpsURLConnection connection;
        InputStream inputStream;
        connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("X-MVG-Authorization-Key", AUTH_KEY);
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.connect();
        inputStream = connection.getInputStream();

        return readFromStream(inputStream);
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
