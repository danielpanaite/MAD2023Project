package com.example.courtreservationapplicationjetpack.models.courts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "court")
data class Court(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var address: String,
    var center: String,
    var sport: String,
    var capacity: Int

)