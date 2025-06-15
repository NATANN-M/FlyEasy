package com.example.flyeasy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.flyeasy.model.SavedFlightEntity;

import java.util.List;

@Dao
public interface SavedFlightDao {
    @Insert
    void insert(SavedFlightEntity flight);

    @Query("SELECT * FROM saved_flights")
    LiveData<List<SavedFlightEntity>> getAllSavedFlights();

    @Delete
    void deleteFlight(SavedFlightEntity flight);

//saved flight count for admin
    @Query("SELECT COUNT(*) FROM flights")
    LiveData<Integer> getFlightCount();

}