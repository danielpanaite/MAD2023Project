package com.example.courtreservationapplicationjetpack.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(

    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var name: String,
    var nickname: String,
    var email: String,
    var address: String,
    var age: Int,
    var phone: String,
    var sportId: Int?, //per identificare lo sport di interesse //forse non serve a niente
    var imageUri: String?
)
