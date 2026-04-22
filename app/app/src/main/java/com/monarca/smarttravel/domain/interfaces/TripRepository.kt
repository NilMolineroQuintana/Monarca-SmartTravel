package com.monarca.smarttravel.domain.interfaces

import com.monarca.smarttravel.domain.model.Trip

/**
 * Interfície del repositori de viatges.
 */
interface TripRepository {
    fun getAllTrips(): List<Trip>
    fun getTripById(tripId: Int): Trip?
    fun addTrip(trip: Trip): Trip?
    fun updateTrip(trip: Trip): Trip?
    fun deleteTrip(tripId: Int): Boolean
    fun updateImage(tripId: Int, newImageResId: Int?): Trip?
    fun getNextUpcomingTrip(): Trip?
}