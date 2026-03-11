package com.example.monarcasmarttravel.domain

interface ItineraryItemRepository {
    suspend fun getItemsByTrip(tripId: Int): List<ItineraryItem>
    suspend fun addItineraryItem(item: ItineraryItem): Boolean
    suspend fun updateItineraryItem(item: ItineraryItem): Boolean
    suspend fun deleteItineraryItem(id: Int): Boolean
}