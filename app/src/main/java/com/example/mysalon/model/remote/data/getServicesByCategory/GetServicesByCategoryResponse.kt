package com.example.mysalon.model.remote.data.getServicesByCategory

data class GetServicesByCategoryResponse(
    val message: String,
    val services: ArrayList<ServiceDetail>,
    val status: Int
)