package com.example.courtreservationapplicationjetpack.firestore

data class Court(
    var id: String = "",
    val name: String = "",
    val address: String = "",
    val center: String = "",
    val sport: String = "",
    val capacity: Int = 0
)
