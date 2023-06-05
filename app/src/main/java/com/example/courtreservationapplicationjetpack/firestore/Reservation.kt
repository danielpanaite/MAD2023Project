package com.example.courtreservationapplicationjetpack.firestore

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

data class Reservation (
    var id: String = "",
    val user: String = "",
    val court: String = "",
    val date: Timestamp = Timestamp.now(),
    val notes: String = "",
    val people: Int = 0,
    val invites: List<String> = listOf()
)

fun Reservation.toDate(): String{
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date.toDate())
}

fun Reservation.toTime(): String{
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date.toDate())
}