package com.example.mysalon.model.remote.data.workingHour

data class WorkingHourResponse(
    val message: String,
    val status: Int,
    val timings: Map<String, Weekday>
)