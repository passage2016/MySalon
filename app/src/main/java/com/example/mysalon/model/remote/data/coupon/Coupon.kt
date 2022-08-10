package com.example.mysalon.model.remote.data.coupon

data class Coupon(
    val couponCode: String,
    val couponId: Int,
    val couponText: String,
    val couponType: String,
    val couponValue: Int,
    val fromDate: String,
    val minimumRqrdCost: Double,
    val terms: String,
    val toDate: String
)