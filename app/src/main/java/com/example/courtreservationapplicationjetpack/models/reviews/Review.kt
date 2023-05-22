package com.example.courtreservationapplicationjetpack.models.reviews

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review")
data class Review(

    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var user: Int,
    var court: Int,
    var date: String,
    var review: String,
    var rating: Int

)
