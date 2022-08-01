package com.example.mysalon.model.remote.data.book

data class BookResponse(
    val appointment: Appointment,
    val message: String,
    val status: Int
)