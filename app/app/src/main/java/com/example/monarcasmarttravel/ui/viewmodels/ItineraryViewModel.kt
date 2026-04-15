package com.example.monarcasmarttravel.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monarcasmarttravel.domain.interfaces.ItineraryRepository
import com.example.monarcasmarttravel.domain.interfaces.TripRepository
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.ui.screens.trip.PlanFormState
import com.example.monarcasmarttravel.ui.screens.trip.PlanType
import com.example.monarcasmarttravel.utils.AppError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ItineraryViewModel @Inject constructor(
    private val repository: ItineraryRepository,
    private val tripRepository: TripRepository
) : ViewModel() {

    private val TAG = "ItineraryViewModel"

    var status by mutableStateOf<AppError?>(null)
        private set

    fun clearStatus() {
        status = null
    }

    private val _currentTripId = MutableStateFlow<Int?>(null)

    val items: StateFlow<List<ItineraryItem>> = _currentTripId
        .filterNotNull()
        .flatMapLatest { tripId -> repository.getItemsByTrip(tripId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val transports = listOf("train", "boat", "flight")
    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun loadItemsByTrip(tripId: Int) {
        Log.d(TAG, "loadItemsByTrip: observant items del viatge id=$tripId")
        _currentTripId.value = tripId
    }

    suspend fun getItemById(id: Int): ItineraryItem? {
        val item = repository.getItemById(id)
        if (item == null) Log.w(TAG, "getItemById: no s'ha trobat item id=$id")
        else Log.d(TAG, "getItemById: trobat item id=$id, tipus=${item.type}")
        return item
    }

    fun addItem(tripId: Int, ruta: String, form: PlanFormState) : Unit {
        Log.d(TAG, "addItem: intent d'afegir item -> tripId=$tripId, ruta=$ruta")

        val planType = PlanType.entries.find { it.route == ruta }
            ?: run {
                Log.e(TAG, "addItem: ruta desconeguda -> '$ruta'")
                status = AppError.UNKNOWN
                return
            }
        val parsedDate = parseDate(form.checkInDate)
            ?: run {
                Log.w(TAG, "addItem: format de data invàlid -> '${form.checkInDate}'")
                status = AppError.NON_EXISTING_DATE
                return
            }

        val validationStatus = validateDate(parsedDate, tripId)
        if (validationStatus.code != AppError.OK.code) {
            Log.w(TAG, "addItem: data fora del rang del viatge -> $parsedDate (tripId=$tripId)")
            status = validationStatus
            return
        }

        val newItem = buildItineraryItem(tripId = tripId, ruta = ruta, planType = planType, form = form, date = parsedDate)

        viewModelScope.launch {
            try {
                val result = repository.addItineraryItem(newItem)
                Log.i(TAG, "addItem: item creat correctament")
                status = AppError.fromCode(result)
            } catch (e: Exception) {
                Log.e(TAG, "addItem: error al crear item", e)
                status = AppError.UNKNOWN
            }
        }
    }

    fun updateItem(itemId: Int, ruta: String, form: PlanFormState) {
        Log.d(TAG, "updateItem: intent d'actualitzar item id=$itemId, ruta=$ruta")

        viewModelScope.launch {
            try {
                val existing = repository.getItemById(itemId)
                    ?: run {
                        status = AppError.NON_EXISTING_ITEM
                        return@launch
                    }

                val parsedDate = parseDate(form.checkInDate)
                    ?: run {
                        status = AppError.NON_EXISTING_DATE
                        return@launch
                    }

                val validationStatus = validateDate(parsedDate, existing.tripId)
                if (validationStatus.code != AppError.OK.code) {
                    status = validationStatus
                    return@launch
                }

                val updated = if (ruta in transports) {
                    existing.copy(
                        price = form.price,
                        origin = form.locationName,
                        destination = form.destination,
                        company = form.company,
                        transportNumber = form.transportNumber,
                        departureDate = parsedDate
                    )
                } else {
                    existing.copy(
                        price = form.price,
                        locationName = form.locationName,
                        address = form.address,
                        checkInDate = parsedDate
                    )
                }

                val result = repository.updateItineraryItem(updated)

                status = AppError.fromCode(result)
                Log.i(TAG, "updateItem: item actualitzat correctament -> id=$itemId")

            } catch (e: Exception) {
                Log.e(TAG, "updateItem: error al actualitzar item id=$itemId", e)
                status = AppError.UNKNOWN
            }
        }
    }

    fun deleteItem(item: ItineraryItem): Unit {
        Log.d(TAG, "deleteItem: intent d'eliminar item id=${item.id}, tipus=${item.type}")
        viewModelScope.launch {
            try {
                val result = repository.deleteItineraryItem(item.id)
                Log.i(TAG, "deleteItem: item eliminat correctament -> id=${item.id}")
                status = AppError.fromCode(result)
            } catch (e: Exception) {
                Log.e(TAG, "deleteItem: error al eliminar item id=${item.id}", e)
                status = AppError.UNKNOWN
            }
        }
    }

    private fun parseDate(value: String): Date? =
        runCatching { dateTimeFormat.parse(value) }.getOrNull()

    private fun buildItineraryItem(
        tripId: Int, ruta: String,
        planType: PlanType, form: PlanFormState, date: Date?
    ): ItineraryItem =
        if (ruta in transports) {
            ItineraryItem(
                id = 0, tripId = tripId, type = planType, price = form.price,
                origin = form.locationName, destination = form.destination,
                company = form.company, transportNumber = form.transportNumber,
                departureDate = date
            )
        } else {
            ItineraryItem(
                id = 0, tripId = tripId, type = planType, price = form.price,
                locationName = form.locationName, address = form.address,
                checkInDate = date
            )
        }

    private fun Date.toMinutes() = time / 60000

    // ── Validació ───────────────────────────────────────────────────
    private fun validateDate(date: Date, tripId: Int): AppError {
        val trip = tripRepository.getTripById(tripId) ?: return AppError.NON_EXISTING_TRIP

        if (date.toMinutes() !in trip.dateIn.toMinutes()..trip.dateOut.toMinutes()) {
            return AppError.ITEM_OUT_OF_RANGE
        }

        return AppError.OK
    }
}