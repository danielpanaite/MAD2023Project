package com.example.courtreservationapplicationjetpack.models.sport

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "sport", primaryKeys = [ "idUser", "sportName"])
data class Sport(

    var idUser: Int,
    var sportName: String,
    @ColumnInfo(name = "masteryLevel")
    var masteryLevel: String?,
    var achievements: String?
)
