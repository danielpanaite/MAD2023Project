package com.example.courtreservationapplicationjetpack.models.achievements

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class Achievements(

    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var sportName: String,
    var idUser: Int,
    var date: String,
    var title: String,
    var description: String?
)
