package com.example.mysalon.model.remote.data.coupon

data class CouponResponse(
    val coupons: ArrayList<Coupon>,
    val message: String,
    val status: Int
)