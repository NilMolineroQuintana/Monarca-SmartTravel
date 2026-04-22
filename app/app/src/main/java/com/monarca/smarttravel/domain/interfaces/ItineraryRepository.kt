package com.monarca.smarttravel.domain.interfaces

import com.monarca.smarttravel.domain.model.ItineraryItem

interface ItineraryRepository {
    suspend fun getItemsByTrip(tripId: Int): List<ItineraryItem>
    fun getItemById(id: Int): ItineraryItem?
    fun addItineraryItem(item: ItineraryItem): Int
    fun updateItineraryItem(item: ItineraryItem): Int
    fun deleteItineraryItem(id: Int): Int
}