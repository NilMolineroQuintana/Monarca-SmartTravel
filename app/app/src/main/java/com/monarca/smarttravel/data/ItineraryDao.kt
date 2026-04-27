package com.monarca.smarttravel.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.monarca.smarttravel.domain.model.ItineraryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ItineraryDao {
    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId")
    fun getItemsByTrip(tripId: Int): Flow<List<ItineraryItem>>

    @Query("SELECT * FROM itinerary_items WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): ItineraryItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: ItineraryItem): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: ItineraryItem): Int

    @Query("DELETE FROM itinerary_items WHERE id = :itemId")
    suspend fun deleteItem(itemId: Int): Int

    @Query("DELETE FROM itinerary_items WHERE tripId = :tripId")
    suspend fun deleteItemsByTrip(tripId: Int): Int
}