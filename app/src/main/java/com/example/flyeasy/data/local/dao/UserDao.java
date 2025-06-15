package com.example.flyeasy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.flyeasy.model.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    long insertUser(UserEntity user);

    @Update
    void updateUser(UserEntity user);



    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    LiveData<UserEntity> getUserByEmail(String email);
    @Query("SELECT * FROM users WHERE userid = :userId")
    LiveData<UserEntity> getUserById(int userId);


    @Query("UPDATE users SET profileImageUri = :imageUri WHERE userId = :userId")
    void updateProfileImage(int userId, String imageUri);

//for admin to count the users
    @Query("SELECT COUNT(*) FROM users")
    LiveData<Integer> getUserCount();

@Query("SELECT * FROM users")
    LiveData<List<UserEntity>> getAllUsers();


    @Delete
    void delete(UserEntity user);
}


