package com.example.courtreservationapplicationjetpack.firestore

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

data class Review(
    var id: String = "",
    var user: String = "",
    var court: String = "",

    var date: Timestamp = Timestamp.now(),
    val review: String? = "",
    val rating: Int = 0,
    var idReservation: String = ""

    )


fun Review.toDate(): String{
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date.toDate())
}

fun Review.toTime(): String{
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date.toDate())
}