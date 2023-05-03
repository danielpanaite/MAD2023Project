package com.example.courtreservationapplicationjetpack.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.example.courtreservationapplicationjetpack.models.courts.CourtDao

/**
 * Database class is the database class that provides the app with instances
 * of the DAOs associated with that database
 */
@Database(entities = [Reservations::class, Court::class], version = 1, exportSchema = false)
abstract class ReservationsDatabase : RoomDatabase() {
    abstract fun reservationsDao(): ReservationsDao
    abstract fun courtDao(): CourtDao

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