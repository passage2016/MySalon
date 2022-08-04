package com.example.mysalon.model.remote.data.product

data class ProductsResponse(
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val message: String,
    val pageNo: Int,
    val pageSize: Int,
    val products: ArrayList<Product>,
    val status: Int,
    val totalPages: Int
)