package com.example.mysalon.model.remote.data.review.getReview

data class GetReviewResponse(
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val message: String,
    val pageNo: Int,
    val pageSize: Int,
    val reviews: ArrayList<Review>,
    val status: Int,
    val totalPages: Int,
    val totalRecords: Int
)