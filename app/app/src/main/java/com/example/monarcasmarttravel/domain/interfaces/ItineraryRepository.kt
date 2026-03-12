package com.example.monarcasmarttravel.domain.interfaces

import com.example.monarcasmarttravel.domain.model.ItineraryItem

interface ItineraryRepository {
    suspend fun getItemsByTrip(tripId: Int): List<ItineraryItem>
    fun addItineraryItem(item: ItineraryItem): Boolean
    fun updateItineraryItem(item: ItineraryItem): Boolean
    fun deleteItineraryItem(id: Int): Boolean
}