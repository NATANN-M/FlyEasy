package com.example.flyeasy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_flights")
public class SavedFlightEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String airline;
    private String origin;
    private String destination;
    private String departureTime;
    private double price;

    public SavedFlightEntity(String airline, String origin, String destination, String departureTime, double price) {
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAirline() { return airline; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public double getPrice() { return price; }
}