package com.example.mysalon.model.remote.data.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    val apiToken: String,
    val createdOn: String,
    var dateOfBirth: String,
    val deletedOn: String,
    var emailId: String,
    val emailVerificationCode: String,
    val evcExpiresOn: String,
    val fcmToken: String,
    var fullName: String,
    val gender: String,
    val ipAddress: String,
    val isActive: Int,
    val isDeleted: Int,
    val isEmailVerified: Int,
    val isMobileVerified: Int,
    val message: String,
    val mobileNo: String,
    val password: String,
    var profilePic: String,
    val status: Int,
    val tokenValidUpTo: String,
    val updatedOn: String,
    val userId: Int
): Parcelable