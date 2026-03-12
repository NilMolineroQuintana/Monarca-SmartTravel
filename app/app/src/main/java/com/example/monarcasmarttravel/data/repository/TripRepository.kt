package com.example.monarcasmarttravel.data.repository

import com.example.monarcasmarttravel.domain.Trip

/**
 * Repositori en memòria per a la gestió de viatges (Trips).
 *
 * Els mètodes públics criden els mètodes del domini [Trip] per mantenir
 * la lògica de negoci centralitzada al model. La llista interna és
 * l'única font de veritat mentre no hi hagi persistència externa.
 */
object TripRepository {

    private val trips = mutableListOf<Trip>()
    private var nextId = 1

    /**
     * Retorna una còpia immutable de tots els viatges.
     */
    fun getAllTrips(): List<Trip> = trips.toList()

    /**
     * Afegeix un viatge a la llista interna assignant-li un ID únic.
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
        return trips.removeIf { it.id == tripId }
    }

    /**
     * Actualitza només la imatge d'un viatge existent.
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