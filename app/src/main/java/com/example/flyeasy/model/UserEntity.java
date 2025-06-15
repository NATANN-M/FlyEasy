package com.example.flyeasy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public int userId;

    public String fullName;
    public String email;
    public String password;
    public String phone;
    public String role;

    public String profileImageUri;

}
