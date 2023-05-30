package com.example.courtreservationapplicationjetpack.models.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user ORDER BY id DESC")
    fun readAllData(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE id =:id")
    fun getUserById(id: Int): Flow<User>

    @Update
    suspend fun update(user: User)

}
