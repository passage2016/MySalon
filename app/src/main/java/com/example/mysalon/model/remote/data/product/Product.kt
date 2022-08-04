package com.example.mysalon.model.remote.data.product

data class Product(
    val description: String,
    val offer: String,
    val offerType: String,
    val offerValidUpTo: String,
    val price: Double,
    val productId: Int,
    val productName: String,
    val productPic: String
)