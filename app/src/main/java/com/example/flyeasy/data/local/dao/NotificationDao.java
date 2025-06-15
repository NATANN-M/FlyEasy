package com.example.flyeasy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.flyeasy.model.NotificationEntity;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert
    void insertNotification(NotificationEntity notification);

    @Update
    void updateNotification(NotificationEntity notification);

    @Query("SELECT * FROM notifications WHERE userId = :userId OR userId = 'all' ORDER BY timestamp DESC")
    LiveData<List<NotificationEntity>> getNotificationsForUser(String userId);

    @Delete
    void delete(NotificationEntity notification);

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    LiveData<List<NotificationEntity>> getAllNotifications();
}
