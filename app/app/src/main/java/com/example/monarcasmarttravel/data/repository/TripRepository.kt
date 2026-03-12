package com.example.monarcasmarttravel.domain.interfaces

import com.example.monarcasmarttravel.domain.model.Trip

/**
 * Interfície del repositori de viatges.
 */
interface TripRepository {
    fun getAllTrips(): List<Trip>
    fun addTrip(trip: Trip): Trip
    fun updateTrip(trip: Trip): Trip?
    fun deleteTrip(tripId: Int): Boolean
    fun updateImage(tripId: Int, newImageResId: Int?): Trip?
    fun getNextUpcomingTrip(): Trip?
}