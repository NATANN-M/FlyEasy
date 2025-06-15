package com.example.flyeasy.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.data.local.dao.BookingDao;
import com.example.flyeasy.model.BookingEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookingRepository {

    private final BookingDao bookingDao;
    private final ExecutorService executorService;

    public BookingRepository(Application application) {
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(application);
        bookingDao = db.bookingDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertBooking(BookingEntity booking) {
        executorService.execute(() -> bookingDao.insertBooking(booking));
    }

    public LiveData<List<BookingEntity>> getAllBookings() {
        return bookingDao.getAllBookings();
    }

    public void delete(BookingEntity booking) {
        executorService.execute(() -> bookingDao.delete(booking));
    }

    public LiveData<List<BookingEntity>> getBookingsByUserId(int userId) {
        return bookingDao.getBookingsByUserId(userId); // return the LiveData from DAO
    }
}
