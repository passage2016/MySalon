package com.example.mysalon.model.remote

import com.example.mysalon.model.remote.data.getBarber.GetBarbersResponse
import com.example.mysalon.model.remote.data.getService.GetServiceResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface BarberApiService {
    @Headers("Content-type: application/json")
    @GET("barber/getBarbers")
    fun getBarbers(): Call<GetBarbersResponse>

    @Headers("Content-type: application/json")
    @POST("barber/getBarberServices1")
    fun getBarberServices(@Body getBarberServicesReq: RequestBody): Call<GetServiceResponse>
}