package com.example.mysalon.model.remote.data.book

data class Service(
    val cost: Double,
    val duration: Double,
    val serviceId: Int,
    val serviceName: String,
    val servicePic: String,
    val serviceType: String
)