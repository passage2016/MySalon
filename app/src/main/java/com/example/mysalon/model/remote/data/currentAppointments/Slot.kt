package com.example.mysalon.model.remote.data.currentAppointments

data class Slot(
    val date: String,
    val day: String,
    val slots: Map<String, Boolean>
)