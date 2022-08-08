package com.example.mysalon.model.remote.data.contacts

data class ContactsResponse(
    val contacts: ArrayList<Contact>,
    val message: String,
    val status: Int
)