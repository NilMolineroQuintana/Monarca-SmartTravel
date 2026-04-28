package com.monarca.smarttravel.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monarca.smarttravel.R
import com.monarca.smarttravel.domain.interfaces.AuthRepository
import com.monarca.smarttravel.domain.interfaces.TripRepository
import com.monarca.smarttravel.domain.model.Trip
import com.monarca.smarttravel.utils.AppError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
    private val repository: TripRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val TAG = "TripViewModel"

    var status by mutableStateOf<AppError?>(null)
        private set

    fun clearStatus() {
        status = null
    }

    private val _currentTripId = MutableStateFlow<Int?>(null)

    val trips: StateFlow<List<Trip>> = repository.getAllTrips()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun loadTrip(tripId: Int) {
        Log.d(TAG, "loadTrip: carregant viatge id=$tripId")
        _currentTripId.value = tripId
    }

    /** Viatge futur més proper o en curs. */
    val nextTrip: StateFlow<Trip?> = trips.map { list ->
        val today = Date()
        list.filter { it.dateOut >= today }
            .minByOrNull { it.dateIn }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

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
     */
    fun addTrip(
        title: String,
        description: String,
        dateIn: Date,
        dateOut: Date
    ) {
        val validation = validateTripFields(title, description, dateIn, dateOut)
        if (validation != AppError.OK) {
            status = validation
            Log.e(TAG, "addTrip: validació fallida -> ${validation.name}")
            return
        }

        viewModelScope.launch {
            try {
                val userId = authRepository.getLoggedUID()
                if (userId == null) {
                    status = AppError.UNKNOWN
                    Log.w(TAG, "addTrip: usuari no autenticat")
                    return@launch
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
                val resultCode = repository.addTrip(trip)
                status = AppError.fromCode(resultCode)
                if (status == AppError.OK) {
                    Log.i(TAG, "addTrip: viatge creat correctament")
                }
            } catch (e: Exception) {
                Log.e(TAG, "addTrip: error al crear viatge", e)
                status = AppError.UNKNOWN
            }
        }
    }

    /**
     * Actualitza els camps d'un viatge existent (títol, descripció i dates).
     */
    fun updateTrip(
        tripId: Int,
        title: String,
        description: String,
        dateIn: Date,
        dateOut: Date
    ) {
        val validation = validateTripFields(title, description, dateIn, dateOut)
        if (validation != AppError.OK) {
            status = validation
            Log.e(TAG, "updateTrip: validació fallida -> ${validation.name}")
            return
        }

        viewModelScope.launch {
            try {
                val existing = repository.getTripById(tripId)
                    ?: run {
                        status = AppError.NON_EXISTING_TRIP
                        Log.w(TAG, "updateTrip: no s'ha trobat id=$tripId")
                        return@launch
                    }

                val updated = existing.copy(
                    title = title.trim(),
                    description = description,
                    dateIn = dateIn,
                    dateOut = dateOut,
                    imageResId = resolveImageForDestination(title) ?: existing.imageResId
                )
                val result = repository.updateTrip(updated)

                status = AppError.fromCode(result)
                if (status == AppError.OK) {
                    Log.i(TAG, "updateTrip: viatge actualitzat -> id=$tripId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "updateTrip: error al actualitzar viatge id=$tripId", e)
                status = AppError.UNKNOWN
            }
        }
    }

    /**
     * Elimina un viatge.
     */
    fun deleteTrip(tripId: Int) {
        Log.d(TAG, "deleteTrip: intent d'eliminar id=$tripId")
        viewModelScope.launch {
            try {
                val resultCode = repository.deleteTrip(tripId)
                status = AppError.fromCode(resultCode)
                if (status == AppError.OK) {
                    Log.i(TAG, "deleteTrip: viatge eliminat -> id=$tripId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "deleteTrip: error al eliminar id=$tripId", e)
                status = AppError.UNKNOWN
            }
        }
    }

    /**
     * Canvia la imatge d'un viatge.
     */
    fun changeTripImage(tripId: Int, newImageResId: Int?) {
        viewModelScope.launch {
            val resultCode = repository.updateImage(tripId, newImageResId)
            if (resultCode == AppError.OK.code) {
                Log.i(TAG, "changeTripImage: imatge actualitzada -> id=$tripId")
            }
        }
    }

    /**
     * Retorna el trip amb l'ID indicat, o null si no existeix.
     */
    suspend fun getTripById(tripId: Int): Trip? = repository.getTripById(tripId)
}