package com.example.mysalon.model.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginApiService {
    @GET("AppUser/login")
    fun login(@Query("username") username: String,
        @Query("password") password: String): Call<NewsResponse>

}