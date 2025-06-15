package com.example.flyeasy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "support_messages")
public class SupportMessageEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public String message;
    public String reply;
    public long timestamp;

    public SupportMessageEntity() {}  // Needed by Room

    public SupportMessageEntity(int userId, String message, String reply, long timestamp) {
        this.userId = userId;
        this.message = message;
        this.reply = reply;
        this.timestamp = timestamp;
    }
}
