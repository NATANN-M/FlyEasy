package com.example.flyeasy.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.data.local.dao.NotificationDao;
import com.example.flyeasy.model.NotificationEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationRepository {

    private final NotificationDao notificationDao;
    private final ExecutorService executorService;

    public NotificationRepository(Context context) {
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(context);
        notificationDao = db.notificationDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertNotification(NotificationEntity notification) {
        FlyEasyDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.insertNotification(notification);

            Log.d("NotificationDebug", "Inserted: Title=" + notification.getTitle()
                    + ", Message=" + notification.getMessage()
                    + ", UserId=" + notification.getUserId()
                    + ", Time=" + notification.getTimestamp());
        });
    }

    public void updateNotification(NotificationEntity notification) {
        executorService.execute(() -> notificationDao.updateNotification(notification));

    }

    public LiveData<List<NotificationEntity>> getNotificationsForUser(String userId) {
        return notificationDao.getNotificationsForUser(userId);
    }

    public void deleteNotification(NotificationEntity notification) {

        executorService.execute(() -> notificationDao.delete(notification));
    }

    public LiveData<List<NotificationEntity>> getAllNotifications() {

        return notificationDao.getAllNotifications();
    }
}
