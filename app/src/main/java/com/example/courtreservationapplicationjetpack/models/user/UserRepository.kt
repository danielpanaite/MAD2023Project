package com.example.courtreservationapplicationjetpack.models.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    /**
     * Retrieve all the reservations from the the given data source.
     */

    suspend fun addUser(user: User)

    fun readAllData(): Flow<List<User>>

    fun getUserById(id: Int): Flow<User>

    suspend fun update(user: User)
}