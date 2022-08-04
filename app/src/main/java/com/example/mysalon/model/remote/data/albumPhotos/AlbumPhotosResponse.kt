package com.example.mysalon.model.remote.data.albumPhotos

data class AlbumPhotosResponse(
    val message: String,
    val photos: ArrayList<Photo>,
    val status: Int
)