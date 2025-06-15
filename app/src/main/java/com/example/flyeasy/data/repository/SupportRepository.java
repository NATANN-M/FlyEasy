package com.example.flyeasy.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.local.FlyEasyDatabase;

import com.example.flyeasy.data.local.dao.SupportDao;
import com.example.flyeasy.model.SupportMessageEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SupportRepository {

    private final SupportDao supportDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SupportRepository(Application application) {
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(application);
        supportDao = db.supportDao();
    }

    public LiveData<List<SupportMessageEntity>> getMessagesForUser(int userId) {
        return supportDao.getMessagesByUserId(userId);
    }



    public void insertMessage(SupportMessageEntity message) {
        executor.execute(() -> supportDao.insertSupport(message));
    }

    public void updateMessage(SupportMessageEntity message) {
        executor.execute(() -> supportDao.updateSupport(message));
    }

    public LiveData<List<SupportMessageEntity>> getAllMessages() {
        return supportDao.getAllMessages();
    }

    public void updateReply(SupportMessageEntity message) {
        executor.execute(() -> supportDao.update(message));
    }

}
