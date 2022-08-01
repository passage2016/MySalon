package com.example.mysalon.model.remote.data.getBarberService

data class GetBarberServiceResponse(
    val message: String,
    val services: ArrayList<Service>,
    val status: Int
)