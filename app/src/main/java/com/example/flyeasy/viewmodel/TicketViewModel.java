package com.example.flyeasy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.repository.TicketRepository;
import com.example.flyeasy.model.TicketEntity;

import java.util.List;

public class TicketViewModel extends AndroidViewModel {

    private final TicketRepository repository;
    private final LiveData<List<TicketEntity>> allTickets;

    public TicketViewModel(@NonNull Application application) {
        super(application);
        repository = new TicketRepository(application);
        allTickets = repository.getAllTickets();
    }

    public void insertTicket(TicketEntity ticket) {
        repository.insertTicket(ticket);
    }

    public LiveData<List<TicketEntity>> getAllTickets() {
        return allTickets;
    }

    public LiveData<TicketEntity> getTicketByBookingId(String bookingId) {
        return repository.getTicketByBookingId(bookingId);
    }



    public void deleteTicket(TicketEntity ticket) {
        repository.deleteTicket(ticket);
    }

}
