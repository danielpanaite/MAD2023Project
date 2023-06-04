package com.example.courtreservationapplicationjetpack.firestore

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

data class Notification(
    var id: String = "",
    val receiver: String = "",
    val sender: String = "",
    val status: String = "",
    val type: String = "",
    val date: Timestamp = Timestamp.now()
)

fun Notification.toDate(): String{
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date.toDate())
}

fun Notification.toTime(): String{
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date.toDate())
}