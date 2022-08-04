package com.example.mysalon.model.remote.data.getService

data class GetServiceResponse(
    val message: String,
    val services: ArrayList<Service>,
    val status: Int
)