package com.example.flyeasy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.repository.SupportRepository;
import com.example.flyeasy.model.SupportMessageEntity;

import java.util.List;

public class SupportViewModel extends AndroidViewModel {

    private final SupportRepository repository;

    public SupportViewModel(@NonNull Application application) {
        super(application);
        repository = new SupportRepository(application);
    }

    public LiveData<List<SupportMessageEntity>> getMessagesForUser(int userId) {
        return repository.getMessagesForUser(userId);
    }

    public LiveData<List<SupportMessageEntity>> getAllMessages() {
        return repository.getAllMessages();
    }

    public void sendMessage(SupportMessageEntity message) {
        repository.insertMessage(message);
    }

    public void sendReply(SupportMessageEntity message) {
        repository.updateMessage(message);
    }


   

    public void updateReply(SupportMessageEntity message) {
        repository.updateReply(message);
    }

}
