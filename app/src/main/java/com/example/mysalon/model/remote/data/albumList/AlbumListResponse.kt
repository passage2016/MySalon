package com.example.mysalon.model.remote.data.albumList

data class AlbumListResponse(
    val albums: ArrayList<Album>,
    val message: String,
    val status: Int
)