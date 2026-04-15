package com.example.monarcasmarttravel.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ItineraryDao {
    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId")
    fun getItemsByTrip(tripId: Int): Flow<List<ItineraryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: ItineraryItem): Long

    @Query("DELETE FROM itinerary_items WHERE id = :itemId")
    suspend fun deleteItem(itemId: Int): Int
}