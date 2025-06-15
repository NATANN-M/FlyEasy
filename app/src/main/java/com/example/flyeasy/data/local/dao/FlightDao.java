package com.example.flyeasy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.flyeasy.model.FlightEntity;

import java.util.List;

@Dao
public interface FlightDao {

    @Insert
    void insertFlight(FlightEntity flight);

    @Update
    void updateFlight(FlightEntity flight);

    @Delete
    void deleteFlight(FlightEntity flight);

    @Query("SELECT * FROM flights")
    LiveData<List<FlightEntity>> getAllFlights();

    @Query("SELECT * FROM flights WHERE " +
            "origin LIKE '%' || :origin || '%' AND " +
            "destination LIKE '%' || :destination || '%' AND " +
            "departureTime LIKE '%' || :departureDate || '%'")
    LiveData<List<FlightEntity>> searchFlights(String origin, String destination, String departureDate);

@Query("SELECT * FROM Flights")
    LiveData<List<FlightEntity>> getAllLocalFlights();
@Query("SELECT * FROM Flights WHERE departureTime > CURRENT_TIMESTAMP ORDER BY departureTime ASC")
LiveData<List<FlightEntity>>getUpcomingFlightsByUserId();
}
