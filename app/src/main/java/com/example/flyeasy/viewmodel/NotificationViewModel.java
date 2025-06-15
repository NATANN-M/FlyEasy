package com.example.flyeasy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.repository.NotificationRepository;
import com.example.flyeasy.model.NotificationEntity;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {

    private final NotificationRepository repository;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        repository = new NotificationRepository(application);
    }

    public void insertNotification(NotificationEntity notification) {
        repository.insertNotification(notification);
    }

    public void updateNotification(NotificationEntity notification) {
        repository.updateNotification(notification);
    }

    public LiveData<List<NotificationEntity>> getNotificationsForUser(String userId) {
        return repository.getNotificationsForUser(userId);
    }


    public LiveData<List<NotificationEntity>> getAllNotifications() {

        return repository.getAllNotifications();
    }

    public void deleteNotification(NotificationEntity notification) {
        repository.deleteNotification(notification);
    }

}
