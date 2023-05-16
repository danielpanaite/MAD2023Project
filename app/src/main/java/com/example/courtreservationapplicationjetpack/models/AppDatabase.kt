package com.example.courtreservationapplicationjetpack.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.example.courtreservationapplicationjetpack.models.courts.CourtDao
import com.example.courtreservationapplicationjetpack.models.reservations.Reservation
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationDao
import com.example.courtreservationapplicationjetpack.models.sport.SportDao
import com.example.courtreservationapplicationjetpack.models.user.User
import com.example.courtreservationapplicationjetpack.models.user.UserDao
import com.example.courtreservationapplicationjetpack.models.sport.Sport


/**
 * Database class is the database class that provides the app with instances
 * of the DAOs associated with that database
 */
@Database(entities = [Reservation::class, Court::class, User::class, Sport::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reservationsDao(): ReservationDao
    abstract fun courtDao(): CourtDao

    abstract fun userDao(): UserDao

    abstract fun sportDao(): SportDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "fields")
                    // Setting this option in your app's database builder means that Room
                    // permanently deletes all data from the tables in your database when it
                    // attempts to perform a migration with no defined migration path.
                    .fallbackToDestructiveMigration()
                    .createFromAsset("database/fields.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}