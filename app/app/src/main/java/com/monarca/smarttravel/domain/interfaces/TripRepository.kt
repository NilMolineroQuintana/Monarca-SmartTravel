package com.monarca.smarttravel.domain.interfaces

import com.monarca.smarttravel.domain.model.Trip
import kotlinx.coroutines.flow.Flow

/**
 * Interfície del repositori de viatges.
 */
interface TripRepository {
    fun getAllTrips(): Flow<List<Trip>>
    fun getTripsByUser(userId: Int): Flow<List<Trip>>
    suspend fun getTripById(tripId: Int): Trip?
    suspend fun addTrip(trip: Trip): Int
    suspend fun updateTrip(trip: Trip): Int
    suspend fun deleteTrip(tripId: Int): Int
    suspend fun updateImage(tripId: Int, newImageResId: Int?): Int
    suspend fun getNextUpcomingTrip(): Trip?
}