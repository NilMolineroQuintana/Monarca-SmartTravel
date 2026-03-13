package com.example.monarcasmarttravel.data.repository

import android.util.Log
import com.example.monarcasmarttravel.data.fakeDB.FakeTripDataSource
import com.example.monarcasmarttravel.domain.interfaces.TripRepository
import com.example.monarcasmarttravel.domain.model.Trip
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementació del repositori de viatges.
 *
 * Delega totes les operacions CRUD al [FakeTripDataSource] (in-memory).
 * La validació de negoci es fa aquí, abans de passar les dades al datasource,
 * seguint el flux:
 * UI → ViewModel → TripRepository (interfície) → TripRepositoryImpl → FakeTripDataSource
 */
@Singleton
class TripRepositoryImpl @Inject constructor() : TripRepository {

    private val TAG = "TripRepositoryImpl"
    private val dataSource = FakeTripDataSource

    override fun getAllTrips(): List<Trip> = dataSource.getAllTrips()

    override fun getTripById(tripId: Int): Trip? =
        dataSource.getAllTrips().find { it.id == tripId }

    /**
     * Valida i afegeix un nou viatge.
     * @throws IllegalArgumentException si les dades no superen la validació.
     */
    override fun addTrip(trip: Trip): Trip {
        validateTrip(trip)
        val created = dataSource.addTrip(trip)
        Log.i(TAG, "addTrip: creat id=${created.id}, títol=${created.title}")
        return created
    }

    /**
     * Valida i actualitza un viatge existent.
     * @throws IllegalArgumentException si les dades no superen la validació.
     * @return El viatge actualitzat, o null si no s'ha trobat.
     */
    override fun updateTrip(trip: Trip): Trip? {
        validateTrip(trip)
        val updated = dataSource.updateTrip(trip)
        if (updated != null) {
            Log.i(TAG, "updateTrip: actualitzat id=${trip.id}")
        } else {
            Log.w(TAG, "updateTrip: no s'ha trobat id=${trip.id}")
        }
        return updated
    }

    override fun deleteTrip(tripId: Int): Boolean {
        val status = dataSource.deleteTrip(tripId)
        if (status) {
            Log.i(TAG, "deleteTrip: eliminat id=$tripId")
        } else {
            Log.w(TAG, "deleteTrip: no s'ha trobat id=$tripId")
        }
        return status
    }

    /**
     * Actualitza la imatge d'un viatge sense revalidar la resta de camps.
     * @return El viatge actualitzat, o null si no s'ha trobat.
     */
    override fun updateImage(tripId: Int, newImageResId: Int?): Trip? =
        dataSource.updateImage(tripId, newImageResId)

    override fun getNextUpcomingTrip(): Trip? {
        val now = Date()
        return dataSource.getAllTrips()
            .filter { it.dateIn >= now }
            .minByOrNull { it.dateIn }
    }

    // ── Validació ───────────────────────────────────────────────────

    private fun validateTrip(trip: Trip) {
        require(trip.title.isNotBlank()) {
            "La destinació no pot estar buida."
        }
        require(trip.description.isNotBlank()) {
            "La descripció no pot estar buida."
        }
        require(trip.dateOut.after(trip.dateIn)) {
            "La data d'inici ha de ser anterior a la data de fi."
        }
    }
}