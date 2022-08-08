package com.example.mysalon.model.remote.data.alert

data class AlertResponse(
    val alert: ArrayList<Alert>,
    val message: String,
    val status: Int
)