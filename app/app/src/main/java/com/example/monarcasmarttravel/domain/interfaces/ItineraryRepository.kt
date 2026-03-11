package com.example.monarcasmarttravel.domain.interfaces

import com.example.monarcasmarttravel.domain.model.ItineraryItem

interface ItineraryItemRepository {
    suspend fun getItemsByTrip(tripId: Int): List<ItineraryItem>
    suspend fun addItineraryItem(item: ItineraryItem): Boolean
    suspend fun updateItineraryItem(item: ItineraryItem): Boolean
    suspend fun deleteItineraryItem(id: Int): Boolean
}