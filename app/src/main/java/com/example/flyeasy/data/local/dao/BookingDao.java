package com.example.flyeasy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.flyeasy.model.BookingEntity;

import java.util.List;

@Dao
public interface BookingDao {
    @Insert
    void insertBooking(BookingEntity booking);


    @Query("SELECT * FROM bookings")
    LiveData<List<BookingEntity>> getAllBookings();

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY id DESC")
    LiveData<List<BookingEntity>> getBookingsByUserId(int userId);
    @Delete
    void delete(BookingEntity booking);


//booking count for admin


    @Query("SELECT COUNT(*) FROM bookings")
    LiveData<Integer> getBookingCount();

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY flightDepartureTime ASC LIMIT 1")
    BookingEntity getNextBooking(int userId);


}



