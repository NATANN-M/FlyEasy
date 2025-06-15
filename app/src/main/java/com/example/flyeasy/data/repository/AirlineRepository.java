package com.example.flyeasy.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.data.local.dao.AirlineDao;
import com.example.flyeasy.model.AirlineEntity;

import java.util.List;

public class AirlineRepository {
    private final AirlineDao airlineDao;

    public AirlineRepository(Application application) {
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(application);
        airlineDao = db.airlineDao();
    }

    public void insertAirline(AirlineEntity airline) {
        FlyEasyDatabase.databaseWriteExecutor.execute(() -> airlineDao.insertAirline(airline));
    }

    public LiveData<List<AirlineEntity>> getAllAirlines() {
        return airlineDao.getAllAirlines();
    }
}
