package com.example.mysalon.model.remote.data.review.getReview

data class Review(
    val comment: String,
    val creationDate: String,
    val fullName: String,
    val profilePic: String,
    val rating: Double,
    val reviewId: Int
)