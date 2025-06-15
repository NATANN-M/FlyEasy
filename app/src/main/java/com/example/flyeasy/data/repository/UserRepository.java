package com.example.flyeasy.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.flyeasy.data.local.FlyEasyDatabase;
import com.example.flyeasy.data.local.dao.UserDao;
import com.example.flyeasy.model.UserEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Context context) {
        FlyEasyDatabase db = FlyEasyDatabase.getDatabase(context);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertUser(UserEntity user) {
        executorService.execute(() -> userDao.insertUser(user));
    }

    public void updateUser(UserEntity user) {
        executorService.execute(() -> userDao.updateUser(user));
    }

    public LiveData<UserEntity> getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public LiveData<UserEntity> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public LiveData<List<UserEntity>> getAllUsers() {
        return userDao.getAllUsers(); // Use the existing userDao instance
    }




    public void deleteUser(UserEntity user) {
        executorService.execute(() ->  userDao.delete(user));
    }

}
