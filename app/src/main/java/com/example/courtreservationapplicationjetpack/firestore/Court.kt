package com.example.courtreservationapplicationjetpack.firestore

data class Court(
    var URL: String? = "",
    var id: String = "",
    val name: String = "",
    val address: String = "",
    val center: String = "",
    val sport: String = "",
    val capacity: Int = 0,
    val citta: String = "",
    val prezzo: Int = 0,
)
