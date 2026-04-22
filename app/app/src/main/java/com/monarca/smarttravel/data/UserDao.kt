package com.monarca.smarttravel.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.monarca.smarttravel.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("INSERT INTO acces_history (userId) VALUES (:userId)")
    suspend fun registerAccess(userId: String)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE userId = :id")
    suspend fun getUserById(id: String): User?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}