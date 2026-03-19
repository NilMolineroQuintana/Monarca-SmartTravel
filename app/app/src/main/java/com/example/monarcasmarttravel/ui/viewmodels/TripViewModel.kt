package com.example.monarcasmarttravel.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.domain.interfaces.TripRepository
import com.example.monarcasmarttravel.domain.model.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel per a la pantalla de viatges.
 *
 * Anotació @HiltViewModel perquè Hilt gestioni el cicle de vida correctament,
 * evitant la pèrdua d'estat en rotar la pantalla o navegar entre pantalles.
 *
 * Segueix el patró MVVM: la UI mai toca ni el domini ni el repositori directament.
 * Flux: UI → TripViewModel → TripRepository (interfície) → TripRepositoryImpl → FakeTripDataSource
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
     * Crea un nou viatge cridant [Trip.addTrip].
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
        return try {
            val trip = Trip(
                id = 0,
                title = title.trim(),
                description = description,
                dateIn = dateIn,
                dateOut = dateOut,
                imageResId = resolveImageForDestination(title),
                userId = userId
            )
            trip.addTrip(repository)
            refreshTrips()
            errorMessage = null
            Log.i(TAG, "addTrip: viatge creat -> destí=$title")
            true
        } catch (e: IllegalArgumentException) {
            errorMessage = e.message
            Log.e(TAG, "addTrip: error de validació -> ${e.message}")
            false
        }
    }

    /**
     * Actualitza els camps editables d'un viatge existent (títol, descripció i dates).
     */
    fun updateTrip(
        tripId: Int,
        title: String,
        description: String,
        dateIn: Date,
        dateOut: Date
    ): Boolean {
        return try {
            val existing = trips.find { it.id == tripId } ?: run {
                errorMessage = "No s'ha trobat el viatge a editar."
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
                true
            } else {
                errorMessage = "No s'ha pogut actualitzar el viatge."
                Log.w(TAG, "updateTrip: error en actualitzar id=$tripId")
                false
            }
        } catch (e: IllegalArgumentException) {
            errorMessage = e.message
            Log.e(TAG, "updateTrip: error de validació -> ${e.message}")
            false
        }
    }

    /**
     * Elimina un viatge cridant [Trip.deleteTrip].
     */
    fun deleteTrip(tripId: Int): Boolean {
        val trip = trips.find { it.id == tripId } ?: run {
            errorMessage = "No s'ha trobat el viatge a eliminar."
            Log.w(TAG, "deleteTrip: viatge no trobat id=$tripId")
            return false
        }
        val result = trip.deleteTrip(repository)
        if (result) {
            refreshTrips()
            errorMessage = null
            Log.i(TAG, "deleteTrip: viatge eliminat -> id=$tripId")
        } else {
            errorMessage = "No s'ha pogut eliminar el viatge."
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
        val result = trip.editTrip(repository, newImageResId)
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