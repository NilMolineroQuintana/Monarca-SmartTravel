package com.monarca.smarttravel.domain.interfaces

import com.monarca.smarttravel.domain.model.ItineraryItem
import kotlinx.coroutines.flow.Flow

interface ItineraryRepository {
    fun getItemsByTrip(tripId: Int): Flow<List<ItineraryItem>>
    suspend fun getItemById(id: Int): ItineraryItem?
    suspend fun addItineraryItem(item: ItineraryItem): Int
    suspend fun updateItineraryItem(item: ItineraryItem): Int
    suspend fun deleteItineraryItem(id: Int): Int
}