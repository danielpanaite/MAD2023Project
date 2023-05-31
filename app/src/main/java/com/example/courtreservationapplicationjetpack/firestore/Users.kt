package com.example.courtreservationapplicationjetpack.firestore


data class Users(
    var id: String = "",
    val name: String? = "",
    val nickname: String? = "",
    val email: String = "",
    val address: String? = "",
    val age: Int? = 0,
    val phone: String? = "",
    val imageUri: String? = "",
    val sportPreferences: MutableList<Sport> = mutableListOf<Sport>()
)


data class Sport(
    val sportName: String = "",
    val masteryLevel: String = "",
)
