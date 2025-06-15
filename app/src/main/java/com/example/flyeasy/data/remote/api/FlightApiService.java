package com.example.flyeasy.data.remote.api;

import com.example.flyeasy.model.api.FlightResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlightApiService {

    @GET("flights")
    Call<FlightResponse> getFlights(
            @Query("access_key") String accessKey,
            @Query("limit") int limit
    );
}
