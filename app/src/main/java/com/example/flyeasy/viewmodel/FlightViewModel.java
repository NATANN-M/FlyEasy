package com.example.flyeasy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.repository.FlightRepository;
import com.example.flyeasy.model.FlightEntity;

import java.util.List;

public class FlightViewModel extends AndroidViewModel {
    private final FlightRepository repository;

    public FlightViewModel(@NonNull Application application) {
        super(application);
        repository = new FlightRepository(application);
    }

    //api data flights getAllFlights()
    public LiveData<List<FlightEntity>> getAllFlights() {
        return repository.getAllFlights();
    }

    public LiveData<List<FlightEntity>> searchFlights(String origin, String destination, String date) {
        return repository.searchFlights(origin, destination, date);
    }


    public LiveData<List<FlightEntity>> getAllLocalFlights() {
        return repository.getAllLocalFlights(); //local saved flights
    }

    public void insertFlight(FlightEntity flight) {
        repository.insertFlight(flight);
    }

    public void deleteFlight(FlightEntity flight) {
        repository.deleteFlight(flight);
    }

    public void updateFlight(FlightEntity flight) {
        repository.updateFlight(flight);
    }

}