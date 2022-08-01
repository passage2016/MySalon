package com.example.mysalon.model.remote

import com.example.mysalon.model.remote.data.book.BookResponse
import com.example.mysalon.model.remote.data.currentAppointments.CurrentAppointmentsResponse
import com.example.mysalon.model.remote.data.login.LoginResponse
import com.example.mysalon.model.remote.data.signUp.SignUpResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AppointmentApiService {
    @Headers("Content-type: application/json")
    @GET
    fun currentAppointments(@Url url: String): Call<CurrentAppointmentsResponse>

    @Headers("Content-type: application/json")
    @POST("/appointment/book")
    fun bookAppointment(@Header("ps_auth_token") ps_auth_token: String, @Body bookReq: RequestBody): Call<BookResponse>

    @Headers("Content-type: application/json")
    @POST("/appointment/reschedule")
    fun rescheduleAppointment(@Header("ps_auth_token") ps_auth_token: String, @Body bookReq: RequestBody): Call<BookResponse>

    @Headers("Content-type: application/json")
    @POST
    fun cancelAppointment(@Url url: String, @Header("ps_auth_token") ps_auth_token: String): Call<BookResponse>
}