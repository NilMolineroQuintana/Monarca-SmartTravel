package com.example.monarcasmarttravel.domain.interfaces

import com.example.monarcasmarttravel.domain.model.ItineraryItem
import kotlinx.coroutines.flow.Flow

interface ItineraryRepository {
    fun getItemsByTrip(tripId: Int): Flow<List<ItineraryItem>>
    fun getItemById(id: Int): ItineraryItem?
    suspend fun addItineraryItem(item: ItineraryItem): Int
    fun updateItineraryItem(item: ItineraryItem): Int
    suspend fun deleteItineraryItem(id: Int): Int
}