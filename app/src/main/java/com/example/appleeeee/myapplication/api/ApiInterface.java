package com.example.appleeeee.myapplication.api;

import com.example.appleeeee.myapplication.model.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

        @GET("public/yql")
        Call<Model> getWeather(@Query("q") String query,
                               @Query("format") String format);
}
