package com.example.flyeasy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "airlines")
public class AirlineEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String logoUrl;
    public String country;
}
