package com.example.courtreservationapplicationjetpack.models.sport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity(tableName = "sport", primaryKeys = [ "idUser", "sportName"])
data class Sport(

    var idUser: Int,
    var sportName: String,
    @ColumnInfo(name = "masteryLevel")
    var masteryLevel: String?,
    //beginner, intermediate, advanced, expert
    var achievements: String?
)
