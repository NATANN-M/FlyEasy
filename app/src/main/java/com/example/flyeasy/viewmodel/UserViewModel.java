package com.example.flyeasy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.repository.UserRepository;
import com.example.flyeasy.model.UserEntity;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void insert(UserEntity user) {
        repository.insertUser(user);
    }

    public void update(UserEntity user) {
        repository.updateUser(user);
    }

    public LiveData<UserEntity> getUserById(int id) {
        return repository.getUserById(id);
    }

    public LiveData<UserEntity> getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    public LiveData<List<UserEntity>> getAllUsers() {
        return repository.getAllUsers();
    }


    public void updateUser(UserEntity user) {
        repository.updateUser(user);
    }

    public void deleteUser(UserEntity user) {
        repository.deleteUser(user);
    }


}
