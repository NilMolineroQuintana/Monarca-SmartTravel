package com.example.monarcasmarttravel.data

import androidx.room.Dao
import androidx.room.Query
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ItineraryDao {
    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId")
    fun getItemsByTrip(tripId: Int): Flow<List<ItineraryItem>>

}