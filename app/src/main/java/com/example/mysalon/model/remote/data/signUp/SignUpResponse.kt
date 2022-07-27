package com.example.mysalon.model.remote.data.signUp

data class SignUpResponse(
    val message: String,
    val otp: String,
    val status: Int,
    val userId: String
)