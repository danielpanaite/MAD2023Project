package com.example.courtreservationapplicationjetpack.models.user

import com.example.courtreservationapplicationjetpack.models.reservations.Reservation
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationDao
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationRepository
import kotlinx.coroutines.flow.Flow


class OfflineUserRepository(private val userDao: UserDao) :
    UserRepository {
    override suspend fun addUser(user: User)= userDao.addUser(user)

    override  fun readAllData(): Flow<List<User>> = userDao.readAllData()

    override fun getUserById(id: Int): Flow<User> = userDao.getUserById(id)

    override suspend fun update(user: User) = userDao.update(user)

}

