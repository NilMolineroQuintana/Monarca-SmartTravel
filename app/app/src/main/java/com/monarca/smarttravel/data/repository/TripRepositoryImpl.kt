package com.monarca.smarttravel.data.repository

import android.util.Log
import com.monarca.smarttravel.data.ItineraryDao
import com.monarca.smarttravel.data.TripDao
import com.monarca.smarttravel.domain.interfaces.TripRepository
import com.monarca.smarttravel.domain.model.Trip
import com.monarca.smarttravel.utils.AppError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    override fun getTripsByUser(userId: String): Flow<List<Trip>> = tripDao.getTripsByUser(userId)

    override suspend fun getTripById(tripId: Int): Trip? = tripDao.getTripById(tripId)

    /**
     * Valida i afegeix un nou viatge a la base de dades.
     */
    override suspend fun addTrip(trip: Trip): Int {
        if (!validateTrip(trip)) return AppError.UNKNOWN.code
        val id = tripDao.addTrip(trip)
        Log.i(TAG, "addTrip: creat id=$id, títol=${trip.title}")
        return if (id > 0) AppError.OK.code else AppError.UNKNOWN.code
    }

    /**
     * Valida i actualitza un viatge existent.
     */
    override suspend fun updateTrip(trip: Trip): Int {
        if (!validateTrip(trip)) return AppError.UNKNOWN.code
        if (!validateItineraryItemsInRange(trip)) return AppError.ITEM_OUT_OF_RANGE.code
        
        val rowsAffected = tripDao.updateTrip(trip)
        if (rowsAffected > 0) {
            Log.i(TAG, "updateTrip: actualitzat id=${trip.id}")
        } else {
            Log.w(TAG, "updateTrip: no s'ha trobat id=${trip.id}")
            return AppError.NON_EXISTING_TRIP.code
        }
        return AppError.OK.code
    }

    /**
     * Elimina un viatge i els seus elements de l'itinerari associats (cascade delete).
     */
    override suspend fun deleteTrip(tripId: Int): Int {
        // Eliminar el viatge
        val deletedTrips = tripDao.deleteTripById(tripId)
        if (deletedTrips > 0) {
            Log.i(TAG, "deleteTrip: viatge eliminat id=$tripId")
        } else {
            Log.w(TAG, "deleteTrip: no s'ha trobat el viatge id=$tripId")
            return AppError.NON_EXISTING_TRIP.code
        }
        return AppError.OK.code
    }

    /**
     * Actualitza únicament la imatge d'un viatge existent.
     * @return El nombre de files afectades.
     */
    override suspend fun updateImage(tripId: Int, newImageResId: Int?): Int {
        val affected = tripDao.updateImage(tripId, newImageResId)
        return if (affected > 0) AppError.OK.code else AppError.NON_EXISTING_TRIP.code
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
     * @return true si tots els items estan dins del rang, false en cas contrari.
     */
    private suspend fun validateItineraryItemsInRange(trip: Trip): Boolean {
        val items = itineraryDao.getItemsByTrip(trip.id).first()
            .filter { it.getInDate() != null }

        if (items.isEmpty()) return true

        val tripStartMin = trip.dateIn.time  / 60_000
        val tripEndMin   = trip.dateOut.time / 60_000

        val firstItem = items.minByOrNull { it.getInDate()!!.time }!!
        val lastItem  = items.maxByOrNull { it.getInDate()!!.time }!!

        if ((firstItem.getInDate()!!.time / 60_000) < tripStartMin) {
            val label = firstItem.locationName ?: firstItem.origin ?: "id=${firstItem.id}"
            Log.w(TAG, "validateItineraryItemsInRange: '$label' queda abans del nou inici -> viatge id=${trip.id}")
            return false
        }

        if ((lastItem.getInDate()!!.time / 60_000) > tripEndMin) {
            val label = lastItem.locationName ?: lastItem.origin ?: "id=${lastItem.id}"
            Log.w(TAG, "validateItineraryItemsInRange: '$label' queda després del nou final -> viatge id=${trip.id}")
            return false
        }

        Log.d(TAG, "validateItineraryItemsInRange: rang vàlid per al viatge id=${trip.id}")
        return true
    }
}