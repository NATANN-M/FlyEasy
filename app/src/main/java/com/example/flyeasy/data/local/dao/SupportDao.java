package com.example.flyeasy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.flyeasy.model.SupportMessageEntity;

import java.util.List;

@Dao
public interface SupportDao {

    @Insert
    void insertSupport(SupportMessageEntity message);

    @Update
    void updateSupport(SupportMessageEntity message);

    @Query("SELECT * FROM support_messages WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<SupportMessageEntity>> getMessagesByUserId(int userId);



    @Query("SELECT * FROM support_messages ORDER BY timestamp DESC")
    LiveData<List<SupportMessageEntity>> getAllMessages();

    @Update
    void update(SupportMessageEntity message);

}
