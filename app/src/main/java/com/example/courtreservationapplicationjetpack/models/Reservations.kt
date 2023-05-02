package com.example.courtreservationapplicationjetpack.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "reservations")
data class Reservations(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var user: Int, //user id of the reservations
    var courtId: Int, // id del campo prenotato
    var date: String,
    var slot: String,
    var additions: String,
    var people: Int,

)