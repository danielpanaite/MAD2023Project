package com.example.courtreservationapplicationjetpack.firestore

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


data class Users(
    var id: String = "",
    val name: String? = "",
    val nickname: String? = "",
    val email: String = "",
    val address: String? = "",
    val age: Int? = 0,
    val phone: String? = "",
    val imageUri: String? = "",
    val sportPreferences: MutableList<Sport> = mutableListOf(),
    val achievements: MutableList<Achievements> = mutableListOf(),
    val friends: MutableList<String> = mutableListOf()

)


data class Sport(
    val sportName: String = "",
    val masteryLevel: String = "",
)

data class Achievements(
    val title: String,
    val sportName: String,
    val date: Timestamp = Timestamp.now(),
    val description: String?,
){
    constructor() : this("", "", Timestamp.now(), "")
}

fun Achievements.toDate(): String{
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date.toDate())
}

fun Achievements.toTime(): String{
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date.toDate())
}


