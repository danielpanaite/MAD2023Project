package com.example.courtreservationapplicationjetpack.models.sport

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity(tableName = "sport")
data class Sport(

    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var idUser: Int,
    var sportName: String,
    var masteryLevel: String?,
    //beginner, intermediate, advanced, expert
    var achievements: String?
)
