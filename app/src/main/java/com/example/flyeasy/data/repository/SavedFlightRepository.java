package com.example.flyeasy.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.data.local.dao.SavedFlightDao;
import com.example.flyeasy.model.SavedFlightEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavedFlightRepository {
    private final SavedFlightDao savedFlightDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SavedFlightRepository(Application application) {
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(application);
        savedFlightDao = db.savedFlightDao();
    }

    public void insert(SavedFlightEntity flight) {
        executor.execute(() -> savedFlightDao.insert(flight));
    }

    public LiveData<List<SavedFlightEntity>> getAllSavedFlights() {
        return savedFlightDao.getAllSavedFlights();
    }

    public void delete(SavedFlightEntity flight) {
        executor.execute(() -> savedFlightDao.deleteFlight(flight));
    }
}