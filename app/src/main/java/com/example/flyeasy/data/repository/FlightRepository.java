package com.example.flyeasy.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.data.local.dao.FlightDao;
import com.example.flyeasy.data.remote.api.ApiClient;
import com.example.flyeasy.data.remote.api.FlightApiService;
import com.example.flyeasy.model.FlightEntity;
import com.example.flyeasy.model.api.FlightResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightRepository {

    private final FlightDao flightDao;
    private final ExecutorService executorService;
    private final FlightApiService apiService;
    private final String API_KEY = "eea3b1c837ffca6d076120bec3d83081"; //   API key

    public FlightRepository(Context context) {
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(context);
        flightDao = db.flightDao();
        executorService = Executors.newSingleThreadExecutor();
        apiService = ApiClient.getClient().create(FlightApiService.class);
    }

    public LiveData<List<FlightEntity>> getAllFlights() {
        MutableLiveData<List<FlightEntity>> liveData = new MutableLiveData<>();

        apiService.getFlights(API_KEY, 100).enqueue(new Callback<FlightResponse>() {
            @Override
            public void onResponse(Call<FlightResponse> call, Response<FlightResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FlightEntity> flightEntities = new ArrayList<>();

                    for (FlightResponse.FlightData data : response.body().data) {
                        FlightEntity flight = new FlightEntity();
                        flight.airline = data.airline.name;
                        flight.origin = data.departure.airport;
                        flight.destination = data.arrival.airport;
                        flight.departureTime = data.departure.scheduled;
                        flight.arrivalTime = data.arrival.scheduled;
                        flight.price = 3000.99;
                        flight.seatsAvailable = 10;
                        flight.imageUrl = "";
                        flightEntities.add(flight);
                    }

                    liveData.postValue(flightEntities);
                } else {
                    liveData.postValue(new ArrayList<>()); // Avoid null crash
                    Log.e("FlightRepo", "API Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<FlightResponse> call, Throwable t) {
                liveData.postValue(new ArrayList<>()); // Avoid null crash
                Log.e("FlightRepo", "API Error: " + t.getMessage());
            }
        });

        return liveData;
    }

    public LiveData<List<FlightEntity>> searchFlights(String origin, String destination, String date) {
        return flightDao.searchFlights(origin, destination, date);
    }

    public void insertFlight(FlightEntity flight) {

        executorService.execute(() -> flightDao.insertFlight(flight));    }

    public void deleteFlight(FlightEntity flight) {
        executorService.execute(() -> flightDao.deleteFlight(flight));
    }

    public LiveData<List<FlightEntity>> getAllLocalFlights() {
        return flightDao.getAllLocalFlights();
    }

    public void updateFlight(FlightEntity flight) {
        executorService.execute(() -> flightDao.updateFlight(flight));
    }

}
