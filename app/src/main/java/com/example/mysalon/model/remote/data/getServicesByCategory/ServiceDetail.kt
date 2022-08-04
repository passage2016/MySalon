package com.example.mysalon.model.remote.data.getServicesByCategory

data class ServiceDetail(
    val cost: Double,
    val description: String,
    val duration: Double,
    val serviceId: Int,
    val serviceName: String,
    val servicePic: String,
    val serviceType: String
)