package com.example.mysalon.model.remote.data.login

data class LoginResponse(
    val apiToken: String,
    val createdOn: String,
    val dateOfBirth: String,
    val deletedOn: String,
    val emailId: String,
    val emailVerificationCode: Any,
    val evcExpiresOn: Any,
    val fcmToken: Any,
    val fullName: String,
    val gender: String,
    val ipAddress: String,
    val isActive: Int,
    val isDeleted: Int,
    val isEmailVerified: Int,
    val isMobileVerified: Int,
    val message: String,
    val mobileNo: String,
    val password: String,
    val profilePic: Any,
    val status: Int,
    val tokenValidUpTo: String,
    val updatedOn: String,
    val userId: Int
)