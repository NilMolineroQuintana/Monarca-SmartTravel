package com.example.monarcasmarttravel.data.repository

import com.example.monarcasmarttravel.domain.Trip

/**
 * Repositori en memòria per a la gestió de viatges (Trips).
 * Implementa les operacions CRUD bàsiques sense persistència externa.
 */
object TripRepository {

    private val trips = mutableListOf<Trip>()
    private var nextId = 1

    fun getAllTrips(): List<Trip> = trips.toList()

    /**
     * Afegeix un nou viatge. L'ID s'assigna internament.
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
     * @return true si s'ha eliminat, false si no s'ha trobat.
     */
    fun deleteTrip(tripId: Int): Boolean {
        return trips.removeIf { it.id == tripId }
    }

    /**
     * Valida les dades d'un viatge.
     * @throws IllegalArgumentException si destinació buida o dates invàlides.
     */
    private fun validateTrip(trip: Trip) {
        require(trip.destination.isNotBlank()) {
            "La destinació no pot estar buida."
        }
        require(trip.dateOut.after(trip.dateIn)) {
            "La data d'inici ha de ser anterior a la data de fi."
        }
    }
}