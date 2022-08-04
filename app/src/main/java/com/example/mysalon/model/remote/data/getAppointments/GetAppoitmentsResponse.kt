package com.example.mysalon.model.remote.data.getAppointments

data class GetAppoitmentsResponse(
    val appointments: ArrayList<AppointmentInfo>,
    val message: String,
    val status: Int
)