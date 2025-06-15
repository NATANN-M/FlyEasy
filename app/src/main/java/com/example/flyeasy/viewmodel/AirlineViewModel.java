package com.example.flyeasy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.repository.AirlineRepository;
import com.example.flyeasy.model.AirlineEntity;

import java.util.List;

public class AirlineViewModel extends AndroidViewModel {
    private final AirlineRepository repository;

    public AirlineViewModel(@NonNull Application application) {
        super(application);
        repository = new AirlineRepository(application);
    }

    public void insert(AirlineEntity airline) {
        repository.insertAirline(airline);
    }

    public LiveData<List<AirlineEntity>> getAllAirlines() {
        return repository.getAllAirlines();
    }
}
