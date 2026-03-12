package com.example.monarcasmarttravel.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.data.repository.TripRepository
import com.example.monarcasmarttravel.domain.Trip
import java.util.Date

/**
 * ViewModel per a la pantalla de viatges.
 *
 * Exposa l'estat observable dels viatges i ofereix les operacions
 * addTrip, deleteTrip i changeTripImage delegant al [TripRepository].
 */
class TripViewModel : ViewModel() {

    private val TAG = "TripViewModel"

    /** Llista observable. La UI es recomposa automàticament quan canvia. */
    var trips by mutableStateOf(TripRepository.getAllTrips())
        private set

    /** Missatge d'error per mostrar com a Snackbar a la UI. */
    var errorMessage by mutableStateOf<String?>(null)
        private set

    private fun refreshTrips() {
        trips = TripRepository.getAllTrips()
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
            lower.contains("new york") || lower.contains("nova york") || lower.contains("nova york") ||
                    lower.contains("nyc") -> R.drawable.newyork
            else -> null
        }
    }

    /**
     * Afegeix un nou viatge. La imatge s'assigna automàticament
     * si el destí coincideix amb una foto disponible.
     */
    fun addTrip(
        destination: String,
        dateIn: Date,
        dateOut: Date,
        userId: Int
    ): Boolean {
        return try {
            val trip = Trip(
                id = 0,
                destination = destination.trim(),
                dateIn = dateIn,
                dateOut = dateOut,
                imageResId = resolveImageForDestination(destination),
                userId = userId
            )
            TripRepository.addTrip(trip)
            refreshTrips()
            errorMessage = null
            Log.i(TAG, "addTrip: viatge creat -> destí=$destination")
            true
        } catch (e: IllegalArgumentException) {
            errorMessage = e.message
            Log.e(TAG, "addTrip: error -> ${e.message}")
            false
        }
    }

    /** Elimina el viatge amb l'ID indicat. */
    fun deleteTrip(tripId: Int): Boolean {
        val result = TripRepository.deleteTrip(tripId)
        if (result) {
            refreshTrips()
            errorMessage = null
            Log.i(TAG, "deleteTrip: viatge eliminat -> id=$tripId")
        } else {
            errorMessage = "No s'ha trobat el viatge a eliminar."
            Log.w(TAG, "deleteTrip: viatge no trobat id=$tripId")
        }
        return result
    }

    /** Canvia la imatge d'un viatge existent. */
    fun changeTripImage(tripId: Int, newImageResId: Int?): Boolean {
        val index = trips.indexOfFirst { it.id == tripId }
        if (index == -1) {
            Log.w(TAG, "changeTripImage: viatge no trobat id=$tripId")
            return false
        }
        // Actualitza directament la llista interna del repositori via deleteTrip + addTrip
        val original = trips[index]
        TripRepository.deleteTrip(tripId)
        return try {
            val updated = original.copy(id = 0, imageResId = newImageResId)
            TripRepository.addTrip(updated)
            refreshTrips()
            Log.i(TAG, "changeTripImage: imatge actualitzada -> id=$tripId")
            true
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "changeTripImage: error -> ${e.message}")
            false
        }
    }

    fun clearError() {
        errorMessage = null
    }
}