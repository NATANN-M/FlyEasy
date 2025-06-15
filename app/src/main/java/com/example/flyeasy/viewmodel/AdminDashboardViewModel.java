package com.example.flyeasy.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.flyeasy.data.local.dao.BookingDao;
import com.example.flyeasy.data.local.dao.SavedFlightDao;
import com.example.flyeasy.data.local.dao.UserDao;

public class AdminDashboardViewModel extends ViewModel {

    private UserDao userDao;
    private BookingDao bookingDao;
    private SavedFlightDao flightDao;


    public AdminDashboardViewModel(UserDao userDao, BookingDao bookingDao,
                                   SavedFlightDao flightDao) {
        this.userDao = userDao;
        this.bookingDao = bookingDao;
        this.flightDao = flightDao;

    }

    public LiveData<Integer> getUserCount() {
        return userDao.getUserCount();
    }

    public LiveData<Integer> getFlightCount() {
        return flightDao.getFlightCount();
    }

    public LiveData<Integer> getBookingCount() {
        return bookingDao.getBookingCount();
    }



    public static class Factory implements ViewModelProvider.Factory {
        private final UserDao userDao;
        private final BookingDao bookingDao;
        private final SavedFlightDao flightDao;


        public Factory(UserDao userDao, BookingDao bookingDao,
                       SavedFlightDao flightDao) {
            this.userDao = userDao;
            this.bookingDao = bookingDao;
            this.flightDao = flightDao;

        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new AdminDashboardViewModel(userDao, bookingDao, flightDao);
        }
    }
}

