package com.example.monarcasmarttravel.data.repository

import com.example.monarcasmarttravel.domain.Trip
import java.util.Date

/**
 * Repositori en memòria per a la gestió de viatges (Trips).
 *
 * Implementa les operacions CRUD bàsiques sense persistència externa.
 * Les dades es guarden NOMÉS en memòria mentre l'app està en execució.
 * Es perdran en tancar l'aplicació
 */
class TripRepository {

    private val trips = mutableListOf<Trip>()
    private var nextId = 1

    fun getAllTrips(): List<Trip> = trips.toList()

    /**
     * Afegeix un viatge assignant-li un ID únic.
     * Cridat des de [Trip.createTrip].
     * @throws IllegalArgumentException si les dades no són vàlides.
     */
    fun addTrip(trip: Trip): Trip {
        validateTrip(trip)
        val newTrip = trip.copy(id = nextId++)
        trips.add(newTrip)
        return newTrip
    }

    /**
     * Elimina el viatge amb l'ID indicat.
     * Cridat des de [Trip.deleteTrip].
     * @return true si s'ha eliminat, false si no s'ha trobat.
     */
    fun deleteTrip(tripId: Int): Boolean {
        val filtered = trips.filter { it.id != tripId }
        if (filtered.size == trips.size) return false
        trips.clear()
        trips.addAll(filtered)
        return true
    }

    /**
     * Actualitza la imatge d'un viatge existent.
     * Cridat des de [Trip.updateTrip].
     * @return El viatge actualitzat, o null si no s'ha trobat.
     */
    fun updateImage(tripId: Int, newImageResId: Int?): Trip? {
        val index = trips.indexOfFirst { it.id == tripId }
        if (index == -1) return null
        val updated = trips[index].copy(imageResId = newImageResId)
        trips[index] = updated
        return updated
    }

    /**
     * Retorna el viatge futur més proper (dateIn >= avui).
     * Si no n'hi ha cap, retorna null.
     */
    fun getNextUpcomingTrip(): Trip? {
        val now = Date()
        return trips
            .filter { it.dateIn >= now }
            .minByOrNull { it.dateIn }
    }

    private fun validateTrip(trip: Trip) {
        require(trip.destination.isNotBlank()) {
            "La destinació no pot estar buida."
        }
        require(trip.dateOut.after(trip.dateIn)) {
            "La data d'inici ha de ser anterior a la data de fi."
        }
    }
}