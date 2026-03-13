package com.example.monarcasmarttravel.data.fakeDB

import android.util.Log
import com.example.monarcasmarttravel.domain.model.Trip
import java.util.Calendar
import com.example.monarcasmarttravel.R
/**
 * Font de dades en memòria per a viatges.
 *
 * Conté un conjunt de viatges predefinits (fake dataset) per facilitar
 * el desenvolupament i les proves sense necessitat de persistència.
 * Les dades es perden en tancar l'aplicació.
 *
 * Segueix el mateix patró que [FakeItineraryItemDataSource].
 */
object FakeTripDataSource {

    private val TAG = "FakeTripDataSource"
    private val calendar = Calendar.getInstance()

    // Fake dataset: viatges precàrregats per poder provar l'aplicació
    private val trips = mutableListOf(
        Trip(
            id = 1,
            title = "Kyoto, Japó",
            description = "Viatge cultural al Japó: temples, geishas i gastronomia tradicional.",
            dateIn = calendar.apply { set(2026, Calendar.MARCH, 23, 10, 30) }.time,
            dateOut = calendar.apply { set(2026, Calendar.MARCH, 30, 15, 0) }.time,
            imageResId = R.drawable.kyoto,
            userId = 1
        ),
        Trip(
            id = 2,
            title = "París, França",
            description = "Escapada romàntica a la ciutat de la llum.",
            dateIn = calendar.apply { set(2026, Calendar.MAY, 15, 8, 30) }.time,
            dateOut = calendar.apply { set(2026, Calendar.MAY, 22, 18, 0) }.time,
            imageResId = R.drawable.paris,
            userId = 1
        ),
        Trip(
            id = 3,
            title = "Nova York, EUA",
            description = "Descobreix la gran poma: Broadway, Central Park i més.",
            dateIn = calendar.apply { set(2026, Calendar.AUGUST, 10, 12, 0) }.time,
            dateOut = calendar.apply { set(2026, Calendar.AUGUST, 25, 11, 0) }.time,
            imageResId = R.drawable.newyork,
            userId = 1
        )
    )

    // Comptador d'IDs: comença des del següent al darrer element del fake dataset
    private var nextId = trips.size + 1

    fun getAllTrips(): List<Trip> = trips.toList()

    fun getTripById(tripId: Int): Trip? = trips.find { it.id == tripId }

    fun addTrip(trip: Trip): Trip {
        val newTrip = trip.copy(id = nextId++)
        trips.add(newTrip)
        Log.i(TAG, "addTrip: viatge afegit id=${newTrip.id}, títol=${newTrip.title}")
        return newTrip
    }

    /**
     * Actualitza tots els camps editables d'un viatge existent.
     * @return El viatge actualitzat, o null si no s'ha trobat.
     */
    fun updateTrip(trip: Trip): Trip? {
        val index = trips.indexOfFirst { it.id == trip.id }
        if (index == -1) {
            Log.w(TAG, "updateTrip: no s'ha trobat el viatge id=${trip.id}")
            return null
        }
        trips[index] = trip
        Log.i(TAG, "updateTrip: viatge actualitzat id=${trip.id}")
        return trips[index]
    }

    fun deleteTrip(tripId: Int): Boolean {
        val status = trips.removeIf { it.id == tripId }
        Log.i(TAG, "deleteTrip: id=$tripId, status=$status")
        return status
    }

    /**
     * Actualitza únicament la imatge d'un viatge existent.
     * @return El viatge actualitzat, o null si no s'ha trobat.
     */
    fun updateImage(tripId: Int, newImageResId: Int?): Trip? {
        val index = trips.indexOfFirst { it.id == tripId }
        if (index == -1) {
            Log.w(TAG, "updateImage: no s'ha trobat el viatge id=$tripId")
            return null
        }
        val updated = trips[index].copy(imageResId = newImageResId)
        trips[index] = updated
        Log.d(TAG, "updateImage: imatge actualitzada id=$tripId")
        return updated
    }
}