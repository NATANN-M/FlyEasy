package com.example.flyeasy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tickets")
public class TicketEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String flight;
    public String airline;
    public String from;
    public String to;
    public String bookingDate;
    public String passenger;
    public String seat;
    public String email;
    public String bookingId;
    public String phone;
    public String passportNumber;
    public double flightPrice;
    public String flightDate;
    public String flightTime;
}
