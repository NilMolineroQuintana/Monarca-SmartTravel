package com.example.monarcasmarttravel.domain.interfaces

import com.example.monarcasmarttravel.domain.model.ItineraryItem

interface ItineraryRepository {
    suspend fun getItemsByTrip(tripId: Int): List<ItineraryItem>
    suspend fun getItemById(id: Int): ItineraryItem?
    suspend fun addItineraryItem(item: ItineraryItem): Int
    suspend fun updateItineraryItem(item: ItineraryItem): Int
    suspend fun deleteItineraryItem(id: Int): Int
}