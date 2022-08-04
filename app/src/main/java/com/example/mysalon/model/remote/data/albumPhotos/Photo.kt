package com.example.mysalon.model.remote.data.albumPhotos

data class Photo(
    val albumId: Int,
    val albumName: String,
    val photoId: Int,
    val photoName: String,
    val photoUrl: String
)