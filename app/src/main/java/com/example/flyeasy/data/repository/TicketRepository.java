package com.example.flyeasy.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.data.local.dao.TicketDao;
import com.example.flyeasy.model.TicketEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketRepository {

    private final TicketDao ticketDao;
    private final ExecutorService executorService;

    public TicketRepository(Application application) {
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(application);
        ticketDao = db.ticketDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertTicket(TicketEntity ticket) {
        executorService.execute(() -> ticketDao.insertTicket(ticket));
    }


    public LiveData<TicketEntity> getTicketByBookingId(String bookingId) {
        return ticketDao.getTicketByBookingId(bookingId);
    }

    public LiveData<List<TicketEntity>> getAllTickets() {
        return ticketDao.getAllTickets();
    }

    public void deleteTicket(TicketEntity ticket) {
        executorService.execute(() -> {
            ticketDao.deleteTicket(ticket);
        });
    }
}