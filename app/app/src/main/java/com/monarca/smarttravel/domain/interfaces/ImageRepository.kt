package com.monarca.smarttravel.domain.interfaces

import com.monarca.smarttravel.domain.model.Image
import com.monarca.smarttravel.domain.model.ItineraryItem
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImagesByTrip(tripId: Int): Flow<List<Image>>
}