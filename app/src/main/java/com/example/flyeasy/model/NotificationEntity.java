package com.example.flyeasy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class NotificationEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String message;
    private String timestamp;
    private boolean isRead;
    private int userId; // "all" broadcast, or specific user ID

    public NotificationEntity(String title, String message, String timestamp, boolean isRead, int userId) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.userId = userId;
    }

    // Getters and Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }
}
