package com.example.mysalon.model.remote.data.getServiceCategory

data class GetServiceCategoryResopnse(
    val message: String,
    val serviceCategories: ArrayList<ServiceCategory>,
    val status: Int
)