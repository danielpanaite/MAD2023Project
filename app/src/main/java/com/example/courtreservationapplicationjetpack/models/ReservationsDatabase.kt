package com.example.courtreservationapplicationjetpack.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Reservations::class], version = 1, exportSchema = false)
abstract class ReservationsDatabase : RoomDatabase() {
    abstract fun reservationsDao(): ReservationsDao
    companion object {
        @Volatile
        private var Instance: ReservationsDatabase? = null

        fun getDatabase(context: Context): ReservationsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ReservationsDatabase::class.java, "reservation_database")
                    // Setting this option in your app's database builder means that Room
                    // permanently deletes all data from the tables in your database when it
                    // attempts to perform a migration with no defined migration path.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}