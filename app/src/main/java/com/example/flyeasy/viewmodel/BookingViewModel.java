package com.example.flyeasy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.repository.BookingRepository;
import com.example.flyeasy.model.BookingEntity;

import java.util.List;

public class BookingViewModel extends AndroidViewModel {

    private final BookingRepository repository;

    public BookingViewModel(@NonNull Application application) {
        super(application);
        repository = new BookingRepository(application);
    }

    public void insertBooking(BookingEntity booking) {
        repository.insertBooking(booking);
    }

    public LiveData<List<BookingEntity>> getAllBookings() {
        return repository.getAllBookings();
    }

    public void deleteBooking(BookingEntity booking)
    {
        repository.delete(booking);
    }


    public LiveData<List<BookingEntity>> getBookingsByUserId(int userId) {  return repository.getBookingsByUserId(userId);}
}

