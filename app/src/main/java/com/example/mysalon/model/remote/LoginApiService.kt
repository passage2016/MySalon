package com.example.mysalon.model.remote

import com.example.mysalon.model.remote.data.login.LoginResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface LoginApiService {
    @Headers("Content-type: application/json")
    @POST("AppUser/login")
    fun login(@Body loginReq: RequestBody): Call<LoginResponse>

}