package com.example.flyeasy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.flyeasy.model.TicketEntity;

import java.util.List;

@Dao
public interface TicketDao {

    @Insert
    void insertTicket(TicketEntity ticket);

    @Query("SELECT * FROM tickets ORDER BY id DESC")
    LiveData<List<TicketEntity>> getAllTickets();

    @Query("SELECT * FROM tickets WHERE bookingId = :bookingId LIMIT 1")
    LiveData<TicketEntity> getTicketByBookingId(String bookingId);

    @Delete
    void deleteTicket(TicketEntity ticket);
}
