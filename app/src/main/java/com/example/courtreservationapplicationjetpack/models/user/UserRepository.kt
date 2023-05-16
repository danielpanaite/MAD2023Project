package com.example.courtreservationapplicationjetpack.models.user
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.courtreservationapplicationjetpack.models.reservations.Reservation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface UserRepository {
    /**
     * Retrieve all the reservations from the the given data source.
     */

    suspend fun addUser(user: User)

    fun readAllData(): Flow<List<User>>

    fun getUserById(id: Int): Flow<User>

    suspend fun update(user: User)
}