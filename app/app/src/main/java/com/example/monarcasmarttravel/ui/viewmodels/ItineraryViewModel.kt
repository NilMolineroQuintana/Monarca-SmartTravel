package com.example.monarcasmarttravel.ui.viewmodels

import android.util.Log
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel  // ← Hilt gestiona la creación del ViewModel
class ItineraryViewModel @Inject constructor(
    private val repository: ItineraryRepository,
    private val tripRepository: TripRepository
) : ViewModel() {

    private val TAG = "ItineraryViewModel"

    private val _items = MutableStateFlow<List<ItineraryItem>>(emptyList())
    val items: StateFlow<List<ItineraryItem>> = _items

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val transports = listOf("train", "boat", "flight")
    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun loadItemsByTrip(tripId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _items.value = repository.getItemsByTrip(tripId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getItemById(id: Int): ItineraryItem? {
        return repository.getItemById(id)
    }

    fun addItem(tripId: Int, ruta: String, form: PlanFormState) : Int {
        val planType = PlanType.entries.find { it.route == ruta } ?: return AppError.UNKNOWN.code
        val parsedDate = parseDate(form.checkInDate) ?: return AppError.NON_EXISTING_DATE.code

        val validationStatus = validateDate(parsedDate, tripId)
        Log.d(TAG, "Date validation status: $validationStatus")
        if (validationStatus != AppError.OK.code) return validationStatus

        val newItem = buildItineraryItem(tripId = tripId, ruta = ruta, planType = planType, form = form, date = parsedDate)

        return repository.addItineraryItem(newItem)
    }

    fun updateItem(itemId: Int, ruta: String, form: PlanFormState): Int {
        val existing = repository.getItemById(itemId) ?: return AppError.NON_EXISTING_ITEM.code
        val parsedDate = parseDate(form.checkInDate) ?: return AppError.NON_EXISTING_DATE.code

        val validationStatus = validateDate(parsedDate, existing.tripId)
        Log.d(TAG, "Date validation status: $validationStatus")
        if (validationStatus != AppError.OK.code) return validationStatus

        val updated = if (ruta in transports) {
            existing.copy(
                price           = form.price,
                origin          = form.locationName,
                destination     = form.destination,
                company         = form.company,
                transportNumber = form.transportNumber,
                departureDate   = parsedDate
            )
        } else {
            existing.copy(
                price        = form.price,
                locationName = form.locationName,
                address      = form.address,
                checkInDate  = parsedDate
            )
        }

        return repository.updateItineraryItem(updated)
    }

    fun deleteItem(item: ItineraryItem) : Int {
        return repository.deleteItineraryItem(item.id)
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
    private fun validateDate(date: Date, tripId: Int): Int {
        val trip = tripRepository.getTripById(tripId) ?: return AppError.NON_EXISTING_TRIP.code

        if (date.toMinutes() !in trip.dateIn.toMinutes()..trip.dateOut.toMinutes()) {
            return AppError.ITEM_OUT_OF_RANGE.code
        }

        return AppError.OK.code
    }
}