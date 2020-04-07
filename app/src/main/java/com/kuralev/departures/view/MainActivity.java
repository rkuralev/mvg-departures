package com.kuralev.departures.view;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kuralev.departures.R;
import com.kuralev.departures.controller.Controller;
import com.kuralev.departures.model.Model;
import com.kuralev.departures.model.departure.Departure;
import com.kuralev.departures.model.station.Station;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {

    //TODO Roman: save and restore list state when switching to other apps

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView stationName;
    private Controller controller;
    private DeparturesListAdapter departuresListAdapter;
    private TextView listEmptyView;
    private ImageView updateLocationButton;
    private Station chosenStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Model model = new Model(this, this);
        controller = new Controller(model);

        init();
    }

    public void init() {
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        stationName = findViewById(R.id.station_name);
        updateLocationButton = findViewById(R.id.update_location);

        departuresListAdapter = controller.getDeparturesListAdapter(this);
        ListView departuresList = findViewById(R.id.list_view);
        departuresList.setAdapter(departuresListAdapter);

        listEmptyView = findViewById(R.id.empty_view);
        departuresList.setEmptyView(listEmptyView);

        setPullToRefreshListener();
        setUpdateLocationButtonOnClickListener();
        setOnStationNameClickListener();

        controller.updateDeparturesList();
    }

    private void setPullToRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (chosenStation != null)
                    controller.updateDeparturesList(chosenStation);
            }
        });
    }

    private void setUpdateLocationButtonOnClickListener() {
        updateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.updateLocation();
            }
        });
    }

    private void setOnStationNameClickListener() {
        stationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Station> nearbyStations = controller.getNearbyStations();

                if (nearbyStations.size() == 0)
                    return;

                final String[] nearbyStationsNames = new String[nearbyStations.size()];

                for (int i = 0; i < nearbyStations.size(); i++)
                    nearbyStationsNames[i] = nearbyStations.get(i).getStationName();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.stations_dialog_title);
                builder.setItems(nearbyStationsNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenStation = nearbyStations.get(which);
                        stationName.setText(chosenStation.getStationName());
                        swipeRefreshLayout.setRefreshing(true);
                        controller.updateDeparturesList(chosenStation);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void showMessage(int messageResourceId) {
        Toast.makeText(this, messageResourceId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void operationFailed(int messageResourceId) {
        showMessage(messageResourceId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            controller.updateLocation();
        } else {
            showMessage(R.string.location_access_required);
        }
    }

    @Override
    public void currentStationChanged(Station station) {
        if (station != null) {
            chosenStation = station;
            stationName.setText(chosenStation.getStationName());
        }
        else {
            showMessage(R.string.stations_data_not_available);
        }
    }

    @Override
    public void departureListUpdated(List<Departure> departures) {
        listEmptyView.setText(R.string.departures_not_found);
        if (departures != null && !departures.isEmpty()) {
            departuresListAdapter.clear();
            departuresListAdapter.addAll(departures);
        }
        else {
            showMessage(R.string.departures_not_found);
        }
    }

    @Override
    public void dataUpdateStarted() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void dataUpdateFinished() {
        swipeRefreshLayout.setRefreshing(false);
    }

}
