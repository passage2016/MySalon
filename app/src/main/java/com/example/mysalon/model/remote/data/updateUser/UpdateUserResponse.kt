package com.example.mysalon.model.remote.data.updateUser

data class UpdateUserResponse(
    val dateOfBirth: String,
    val emailId: String,
    val fullName: String,
    val message: String,
    val password: String,
    val profilePic: String,
    val status: Int,
    val userId: String
)