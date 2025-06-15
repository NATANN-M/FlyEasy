package com.example.flyeasy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.example.flyeasy.model.AirlineEntity;

import java.util.List;

@Dao
public interface AirlineDao {

    // Insert a new airline
    @Insert
    void insertAirline(AirlineEntity airline);

    // Update an existing airline
    @Update
    void updateAirline(AirlineEntity airline);

    // Delete an airline
    @Delete
    void deleteAirline(AirlineEntity airline);

    // Get all airlines as LiveData
    @Query("SELECT * FROM airlines")
    LiveData<List<AirlineEntity>> getAllAirlines();

    // Optionally get a specific airline by ID
    @Query("SELECT * FROM airlines WHERE id = :airlineId")
    LiveData<AirlineEntity> getAirlineById(int airlineId);
}
