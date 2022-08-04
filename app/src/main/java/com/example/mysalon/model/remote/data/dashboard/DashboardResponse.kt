package com.example.mysalon.model.remote.data.dashboard

data class DashboardResponse(
    val alertMessage: String,
    val banners: List<Banner>,
    val isShopOpened: String,
    val message: String,
    val status: Int
)