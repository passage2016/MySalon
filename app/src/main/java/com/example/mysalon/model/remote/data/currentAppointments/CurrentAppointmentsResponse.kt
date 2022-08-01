package com.example.mysalon.model.remote.data.currentAppointments

data class CurrentAppointmentsResponse(
    val message: String,
    val slots: ArrayList<Slot>,
    val status: Int
)