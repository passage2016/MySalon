package com.example.mysalon.model.remote

import com.example.mysalon.model.remote.data.dashboard.DashboardResponse
import com.example.mysalon.model.remote.data.login.LoginResponse
import com.example.mysalon.model.remote.data.signUp.SignUpResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AppUserApiService {
    @Headers("Content-type: application/json")
    @POST("appUser/login")
    fun login(@Body loginReq: RequestBody): Call<LoginResponse>

    @Headers("Content-type: application/json")
    @POST("appUser/signup")
    fun signUp(@Body signUpReq: RequestBody): Call<SignUpResponse>

    @Headers("Content-type: application/json")
    @GET("appUser/dashboard")
    fun dashboard(): Call<DashboardResponse>

}