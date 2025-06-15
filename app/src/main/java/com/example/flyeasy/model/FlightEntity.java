package com.example.flyeasy.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "flights")
public class FlightEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String airline;
    public String origin;
    public String destination;
    public String departureTime;
    public String arrivalTime;
    public double price;

    public int seatsAvailable;
    public String imageUrl;

    public FlightEntity() {}

//    protected FlightEntity(Parcel in) {
//        id = in.readInt();
//        airline = in.readString();
//        origin = in.readString();
//        destination = in.readString();
//        departureTime = in.readString();
//        arrivalTime = in.readString();
//        price = in.readDouble();
//        seatsAvailable = in.readInt();
//        imageUrl = in.readString();
//    }
//
//    public static final Creator<FlightEntity> CREATOR = new Creator<FlightEntity>() {
//        @Override
//        public FlightEntity createFromParcel(Parcel in) {
//            return new FlightEntity(in);
//        }
//
//        @Override
//        public FlightEntity[] newArray(int size) {
//            return new FlightEntity[size];
//        }
//    };
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeString(airline);
//        dest.writeString(origin);
//        dest.writeString(destination);
//        dest.writeString(departureTime);
//        dest.writeString(arrivalTime);
//        dest.writeDouble(price);
//        dest.writeInt(seatsAvailable);
//        dest.writeString(imageUrl);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }


}
