package com.example.courtreservationapplicationjetpack.models.reservations

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 Room entities rapresent tables in the app's database. Used to update tha date stored in rows tables
 and create new rows for insertion.
 An entity class defines a table, each instance of this class rapresents a row in the database table.
 The entity class has mapping to tell Room how it intends to present and intercat with the information
 in the database (entritu class name = reservations)
 For each entity class the app creates a database table to hold the reservations
 Data classes are primarly used to hold data in Kotlin.
 */
@Entity(tableName = "reservation")
data class Reservation(

    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var user: Int, //user id of the reservations
    var courtId: Int, // id del campo prenotato
    var date: String,
    var slot: String,
    var additions: String,
    var people: Int,

)

