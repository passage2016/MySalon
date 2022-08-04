package com.example.mysalon.viewModel

data class PageTool(
    val pageStart: Int = 0,
    var isLoading: Boolean = false,
    var isLastPage: Boolean = false,
    var totalPage: Int,
    var currentPage: Int,
    val pageSize: Int
)
