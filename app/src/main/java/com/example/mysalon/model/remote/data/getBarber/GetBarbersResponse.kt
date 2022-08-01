package com.example.mysalon.model.remote.data.getBarber

data class GetBarbersResponse(
    val barbers: ArrayList<Barber>,
    val message: String,
    val status: Int
)