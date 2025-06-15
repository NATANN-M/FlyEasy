package com.example.flyeasy.model.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FlightResponse {

    @SerializedName("data")
    public List<FlightData> data;

    public static class FlightData {
        @SerializedName("airline")
        public Airline airline;

        @SerializedName("departure")
        public Location departure;

        @SerializedName("arrival")
        public Location arrival;

        @SerializedName("flight")
        public FlightInfo flight;

        @SerializedName("flight_status")
        public String flight_status;
    }

    public static class Airline {
        @SerializedName("name")
        public String name;
    }

    public static class Location {
        @SerializedName("airport")
        public String airport;

        @SerializedName("scheduled")
        public String scheduled;
    }

    public static class FlightInfo {
        @SerializedName("iata")
        public String iata;
    }
}
