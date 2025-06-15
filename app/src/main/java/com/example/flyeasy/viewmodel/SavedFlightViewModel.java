package com.example.flyeasy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.repository.SavedFlightRepository;
import com.example.flyeasy.model.SavedFlightEntity;

import java.util.List;

public class SavedFlightViewModel extends AndroidViewModel {
    private final SavedFlightRepository repository;
    private final LiveData<List<SavedFlightEntity>> allSavedFlights;

    public SavedFlightViewModel(@NonNull Application application) {
        super(application);
        repository = new SavedFlightRepository(application);
        allSavedFlights = repository.getAllSavedFlights();
    }

    public void insert(SavedFlightEntity flight) {
        repository.insert(flight);
    }

    public LiveData<List<SavedFlightEntity>> getAllSavedFlights() {
        return allSavedFlights;
    }

    public void delete(SavedFlightEntity flight) {
        repository.delete(flight);
    }
}