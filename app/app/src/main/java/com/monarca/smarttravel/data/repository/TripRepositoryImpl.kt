package com.monarca.smarttravel.data.repository

import android.util.Log
import com.monarca.smarttravel.data.ItineraryDao
import com.monarca.smarttravel.data.TripDao
import com.monarca.smarttravel.domain.interfaces.TripRepository
import com.monarca.smarttravel.domain.model.Trip
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementació del repositori de viatges utilitzant Room.
 */
@Singleton
class TripRepositoryImpl @Inject constructor(
    private val tripDao: TripDao,
    private val itineraryDao: ItineraryDao
) : TripRepository {

    private val TAG = "TripRepositoryImpl"

    override fun getAllTrips(): Flow<List<Trip>> = tripDao.getAllTrips()

    override fun getTripsByUser(userId: Int): Flow<List<Trip>> = tripDao.getTripsByUser(userId)

    override suspend fun getTripById(tripId: Int): Trip? = tripDao.getTripById(tripId)

    /**
     * Valida i afegeix un nou viatge a la base de dades.
     */
    override suspend fun addTrip(trip: Trip): Long {
        if (!validateTrip(trip)) return -1L
        val id = tripDao.addTrip(trip)
        Log.i(TAG, "addTrip: creat id=$id, títol=${trip.title}")
        return id
    }

    /**
     * Valida i actualitza un viatge existent.
     */
    override suspend fun updateTrip(trip: Trip): Int {
        if (!validateTrip(trip)) return 0
        if (!validateItineraryItemsInRange(trip)) return 0
        
        val rowsAffected = tripDao.updateTrip(trip)
        if (rowsAffected > 0) {
            Log.i(TAG, "updateTrip: actualitzat id=${trip.id}")
        } else {
            Log.w(TAG, "updateTrip: no s'ha trobat id=${trip.id}")
        }
        return rowsAffected
    }

    /**
     * Elimina un viatge i els seus elements de l'itinerari associats (cascade delete).
     */
    override suspend fun deleteTrip(tripId: Int): Int {
        // Eliminar elements de l'itinerari primer
        val deletedItems = itineraryDao.deleteItemsByTrip(tripId)
        Log.i(TAG, "deleteTrip: eliminats $deletedItems items de l'itinerari del viatge id=$tripId")

        // Eliminar el viatge
        val deletedTrips = tripDao.deleteTripById(tripId)
        if (deletedTrips > 0) {
            Log.i(TAG, "deleteTrip: viatge eliminat id=$tripId")
        } else {
            Log.w(TAG, "deleteTrip: no s'ha trobat el viatge id=$tripId")
        }
        return deletedTrips
    }

    /**
     * Actualitza únicament la imatge d'un viatge existent.
     * @return El viatge actualitzat, o null si no s'ha trobat.
     */
    override suspend fun updateImage(tripId: Int, newImageResId: Int?): Int {
        return tripDao.updateImage(tripId, newImageResId)
    }

    override suspend fun getNextUpcomingTrip(): Trip? {
        return tripDao.getNextUpcomingTrip(System.currentTimeMillis())
    }

    // ── Validació ───────────────────────────────────────────────────

    /**
     * Valida els camps obligatoris d'un viatge.
     * @return true si la validació passa, false en cas contrari.
     */
    private fun validateTrip(trip: Trip): Boolean {
        if (trip.title.isBlank()) {
            Log.w(TAG, "validateTrip: títol buit -> id=${trip.id}")
            return false
        }
        if (trip.description.isBlank()) {
            Log.w(TAG, "validateTrip: descripció buida -> id=${trip.id}")
            return false
        }
        if (!trip.dateOut.after(trip.dateIn)) {
            Log.w(TAG, "validateTrip: rang de dates invàlid -> id=${trip.id}")
            return false
        }
        return true
    }

    /**
     * Comprova que el primer i l'últim element de l'itinerari del viatge
     * quedin dins del nou rang [trip.dateIn, trip.dateOut].
     *
     * Els items estan ordenats per data, de manera que si el primer i l'últim
     * estan dins del rang, tots els del mig també ho estaran — no cal iterar-los.
     *
     * Utilitza precisió de minuts per ser consistent amb [ItineraryViewModel.validateDate].
     *
     * @return true si tots els items estan dins del rang, false en cas contrari.
     */
    private suspend fun validateItineraryItemsInRange(trip: Trip): Boolean {
        val items = itineraryDao.getItemsByTripSync(trip.id)
            .filter { it.getInDate() != null }

        if (items.isEmpty()) return true

        val tripStartMin = trip.dateIn.time  / 60_000
        val tripEndMin   = trip.dateOut.time / 60_000

        val first = items.minByOrNull { it.getInDate()!!.time }!!
        val last  = items.maxByOrNull { it.getInDate()!!.time }!!

        if (first.getInDate()!!.time / 60_000 < tripStartMin) {
            val label = first.locationName ?: first.origin ?: "id=${first.id}"
            Log.w(TAG, "validateItineraryItemsInRange: '$label' queda abans del nou inici -> viatge id=${trip.id}")
            return false
        }

        if (last.getInDate()!!.time / 60_000 > tripEndMin) {
            val label = last.locationName ?: last.origin ?: "id=${last.id}"
            Log.w(TAG, "validateItineraryItemsInRange: '$label' queda després del nou final -> viatge id=${trip.id}")
            return false
        }

        Log.d(TAG, "validateItineraryItemsInRange: rang vàlid per al viatge id=${trip.id}")
        return true
    }
}