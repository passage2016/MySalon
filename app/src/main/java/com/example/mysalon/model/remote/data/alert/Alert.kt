package com.example.mysalon.model.remote.data.alert

data class Alert(
    val createdOn: String,
    val id: Int,
    val message: String,
    val type: String
)