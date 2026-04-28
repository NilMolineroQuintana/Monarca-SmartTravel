package com.monarca.smarttravel.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.monarca.smarttravel.domain.model.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips")
    fun getAllTrips(): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE userId = :userId")
    fun getTripsByUser(userId: String): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE id = :tripId")
    suspend fun getTripById(tripId: Int): Trip?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrip(trip: Trip): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTrip(trip: Trip): Int

    @Query("DELETE FROM trips WHERE id = :tripId")
    suspend fun deleteTripById(tripId: Int): Int

    @Query("UPDATE trips SET imageResId = :newImageResId WHERE id = :tripId")
    suspend fun updateImage(tripId: Int, newImageResId: Int?): Int

    @Query("SELECT * FROM trips WHERE dateIn >= :now ORDER BY dateIn ASC LIMIT 1")
    suspend fun getNextUpcomingTrip(now: Long): Trip?
}
