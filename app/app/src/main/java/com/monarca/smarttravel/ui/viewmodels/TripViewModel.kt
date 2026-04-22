package com.monarca.smarttravel.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.monarca.smarttravel.R
import com.monarca.smarttravel.domain.interfaces.TripRepository
import com.monarca.smarttravel.domain.model.Trip
import com.monarca.smarttravel.utils.AppError
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel per a la pantalla de viatges.
 *
 * Anotació @HiltViewModel perquè Hilt gestioni el cicle de vida correctament,
 * evitant la pèrdua d'estat en rotar la pantalla o navegar entre pantalles.
 */
@HiltViewModel
class TripViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    private val TAG = "TripViewModel"

    /** Llista observable. La UI es recomposa automàticament quan canvia. */
    var trips by mutableStateOf(repository.getAllTrips())
        private set

    /** Missatge d'error per mostrar com a Snackbar a la UI. */
    var errorMessage by mutableStateOf<String?>(null)
        private set

    private fun refreshTrips() {
        trips = repository.getAllTrips()
    }

    /**
     * Mapeja paraules clau del destí a un drawable existent al projecte.
     * Retorna null si no coincideix cap destí conegut.
     */
    fun resolveImageForDestination(destination: String): Int? {
        val lower = destination.lowercase()
        return when {
            lower.contains("kyoto") || lower.contains("japó") || lower.contains("japo") ||
                    lower.contains("japon") || lower.contains("japan") -> R.drawable.kyoto
            lower.contains("paris") || lower.contains("parís") ||
                    lower.contains("frança") || lower.contains("france") || lower.contains("francia") -> R.drawable.paris
            lower.contains("new york") || lower.contains("nova york") ||
                    lower.contains("nyc") -> R.drawable.newyork
            else -> null
        }
    }

    /**
     * Valida els camps d'un viatge i retorna el [AppError] corresponent.
     * Retorna [AppError.OK] si totes les validacions passen.
     */
    private fun validateTripFields(title: String, description: String, dateIn: Date, dateOut: Date): AppError {
        if (title.isBlank()) {
            Log.w(TAG, "validateTripFields: títol buit")
            return AppError.INVALID_TITLE
        }
        if (description.isBlank()) {
            Log.w(TAG, "validateTripFields: descripció buida")
            return AppError.INVALID_DESCRIPTION
        }
        if (!dateOut.after(dateIn)) {
            Log.w(TAG, "validateTripFields: rang de dates invàlid -> dateIn=$dateIn, dateOut=$dateOut")
            return AppError.INVALID_DATE_RANGE
        }
        return AppError.OK
    }

    /**
     * Crea un nou viatge.
     * Valida les dades abans d'enviar-les al repositori.
     * La imatge s'assigna automàticament si el destí coincideix
     * amb una foto disponible al projecte.
     */
    fun addTrip(
        title: String,
        description: String,
        dateIn: Date,
        dateOut: Date,
        userId: Int
    ): Boolean {
        val validation = validateTripFields(title, description, dateIn, dateOut)
        if (validation != AppError.OK) {
            errorMessage = validation.name
            Log.e(TAG, "addTrip: validació fallida -> ${validation.name}")
            return false
        }

        val trip = Trip(
            id = 0,
            title = title.trim(),
            description = description,
            dateIn = dateIn,
            dateOut = dateOut,
            imageResId = resolveImageForDestination(title),
            userId = userId
        )
        repository.addTrip(trip)
        refreshTrips()
        errorMessage = null
        Log.i(TAG, "addTrip: viatge creat -> destí=$title")
        return true
    }

    /**
     * Actualitza els camps d'un viatge existent (títol, descripció i dates).
     * Valida les dades abans d'enviar-les al repositori.
     */
    fun updateTrip(
        tripId: Int,
        title: String,
        description: String,
        dateIn: Date,
        dateOut: Date
    ): Boolean {
        val validation = validateTripFields(title, description, dateIn, dateOut)
        if (validation != AppError.OK) {
            errorMessage = validation.name
            Log.e(TAG, "updateTrip: validació fallida -> ${validation.name}")
            return false
        }

        val existing = trips.find { it.id == tripId }
        if (existing == null) {
            errorMessage = AppError.NON_EXISTING_ITEM.name
            Log.w(TAG, "updateTrip: viatge no trobat id=$tripId")
            return false
        }

        val updated = existing.copy(
            title = title.trim(),
            description = description,
            dateIn = dateIn,
            dateOut = dateOut,
            imageResId = resolveImageForDestination(title) ?: existing.imageResId
        )
        val result = repository.updateTrip(updated)
        if (result != null) {
            refreshTrips()
            errorMessage = null
            Log.i(TAG, "updateTrip: viatge actualitzat -> id=$tripId, destí=$title")
            return true
        } else {
            errorMessage = AppError.NON_EXISTING_ITEM.name
            Log.w(TAG, "updateTrip: error en actualitzar id=$tripId")
            return false
        }
    }

    /**
     * Elimina un viatge.
     */
    fun deleteTrip(tripId: Int): Boolean {
        val trip = trips.find { it.id == tripId }
        if (trip == null) {
            errorMessage = AppError.NON_EXISTING_ITEM.name
            Log.w(TAG, "deleteTrip: viatge no trobat id=$tripId")
            return false
        }
        val result = repository.deleteTrip(tripId)
        if (result) {
            refreshTrips()
            errorMessage = null
            Log.i(TAG, "deleteTrip: viatge eliminat -> id=$tripId")
        } else {
            errorMessage = AppError.NON_EXISTING_ITEM.name
            Log.w(TAG, "deleteTrip: error en eliminar id=$tripId")
        }
        return result
    }

    /**
     * Canvia la imatge d'un viatge cridant [Trip.editTrip].
     */
    fun changeTripImage(tripId: Int, newImageResId: Int?): Boolean {
        val trip = trips.find { it.id == tripId } ?: run {
            Log.w(TAG, "changeTripImage: viatge no trobat id=$tripId")
            return false
        }
        val result = repository.updateImage(tripId, newImageResId)
        return if (result != null) {
            refreshTrips()
            Log.i(TAG, "changeTripImage: imatge actualitzada -> id=$tripId")
            true
        } else {
            Log.w(TAG, "changeTripImage: error en actualitzar id=$tripId")
            false
        }
    }

    /**
     * Retorna el trip amb l'ID indicat, o null si no existeix.
     * Usat per [ItineraryScreen] per obtenir les dades reals del viatge.
     */
    fun getTripById(tripId: Int): Trip? = trips.find { it.id == tripId }

    fun getNextUpcomingTrip(): Trip? = repository.getNextUpcomingTrip()

    fun clearError() {
        errorMessage = null
    }
}