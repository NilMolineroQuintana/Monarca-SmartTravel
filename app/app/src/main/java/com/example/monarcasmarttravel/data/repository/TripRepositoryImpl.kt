package com.example.monarcasmarttravel.data.repository

import android.util.Log
import com.example.monarcasmarttravel.data.fakeDB.FakeItineraryItemDataSource
import com.example.monarcasmarttravel.data.fakeDB.FakeTripDataSource
import com.example.monarcasmarttravel.domain.interfaces.TripRepository
import com.example.monarcasmarttravel.domain.model.Trip
import com.example.monarcasmarttravel.utils.AppError
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementació del repositori de viatges.
 *
 * Delega totes les operacions CRUD al [FakeTripDataSource] (in-memory).
 */
@Singleton
class TripRepositoryImpl @Inject constructor() : TripRepository {

    private val TAG = "TripRepositoryImpl"
    private val dataSource = FakeTripDataSource
    private val itineraryDataSource = FakeItineraryItemDataSource

    override fun getAllTrips(): List<Trip> = dataSource.getAllTrips()

    override fun getTripById(tripId: Int): Trip? =
        dataSource.getAllTrips().find { it.id == tripId }

    /**
     * Valida i afegeix un nou viatge.
     * Retorna el viatge creat, o null si les dades no superen la validació.
     */
    override fun addTrip(trip: Trip): Trip? {
        if (!validateTrip(trip)) return null
        val created = dataSource.addTrip(trip)
        Log.i(TAG, "addTrip: creat id=${created.id}, títol=${created.title}")
        return created
    }

    /**
     * Valida i actualitza un viatge existent.
     *
     * A més de la validació de camps bàsica, comprova que cap element de
     * l'itinerari existent quedi fora del nou rang de dates del viatge.
     * Ampliar les dates sempre és permès; reduir-les només ho és si cap
     * item queda fora del nou rang.
     *
     * @return El viatge actualitzat, o null si no s'ha trobat o la validació falla.
     */
    override fun updateTrip(trip: Trip): Trip? {
        if (!validateTrip(trip)) return null
        if (!validateItineraryItemsInRange(trip)) return null
        val status = dataSource.updateTrip(trip)
        return if (status == AppError.OK.code) {
            Log.i(TAG, "updateTrip: actualitzat id=${trip.id}")
            dataSource.getTripById(trip.id)
        } else {
            Log.w(TAG, "updateTrip: no s'ha trobat id=${trip.id}")
            null
        }
    }

    /**
     * Elimina un viatge i tots els elements de l'itinerari associats (cascade delete).
     *
     * @return true si el viatge s'ha eliminat correctament, false si no s'ha trobat.
     */
    override fun deleteTrip(tripId: Int): Boolean {
        // Obté els items de l'itinerari abans d'eliminar el viatge
        val itemsToDelete = itineraryDataSource.getItemsByTrip(tripId)

        // Elimina cada item de l'itinerari associat al viatge (cascade)
        itemsToDelete.forEach { item ->
            itineraryDataSource.deleteItem(item.id)
            Log.d(TAG, "deleteTrip: eliminat item id=${item.id} del viatge id=$tripId")
        }
        Log.i(TAG, "deleteTrip: eliminats ${itemsToDelete.size} items de l'itinerari del viatge id=$tripId")

        // Elimina el viatge
        val status = dataSource.deleteTrip(tripId)
        return if (status == AppError.OK.code) {
            Log.i(TAG, "deleteTrip: viatge eliminat id=$tripId")
            true
        } else {
            Log.w(TAG, "deleteTrip: no s'ha trobat el viatge id=$tripId")
            false
        }
    }

    /**
     * Actualitza únicament la imatge d'un viatge existent.
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
    private fun validateItineraryItemsInRange(trip: Trip): Boolean {
        val items = itineraryDataSource.getItemsByTrip(trip.id)
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