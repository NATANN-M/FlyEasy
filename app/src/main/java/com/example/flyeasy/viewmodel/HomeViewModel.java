package com.example.flyeasy.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.repository.FlightRepository;
import com.example.flyeasy.data.repository.SupportRepository;
import com.example.flyeasy.model.FlightEntity;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final FlightRepository flightRepo;
    private final SupportRepository supportRepo;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        flightRepo = new FlightRepository(application);
        supportRepo = new SupportRepository(application);
    }

//    public LiveData<List<FlightEntity>> getUpcomingFlights( ) {
//        return flightRepo.getUpcomingFlightsByUserId();
//    }

//    public LiveData<String> getLatestSupportReply(int userId) {
//        return supportRepo.getLatestReplyForUser(userId);
//    }
}
