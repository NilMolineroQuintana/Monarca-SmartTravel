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

@HiltViewModel
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
            Log.d(TAG, "loadItemsByTrip: carregant items del viatge id=$tripId")
            try {
                _items.value = repository.getItemsByTrip(tripId)
                Log.i(TAG, "loadItemsByTrip: ${_items.value.size} items carregats per tripId=$tripId")
            } catch (e: Exception) {
                _error.value = e.message
                Log.e(TAG, "loadItemsByTrip: error inesperat -> ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getItemById(id: Int): ItineraryItem? {
        val item = repository.getItemById(id)
        if (item == null) Log.w(TAG, "getItemById: no s'ha trobat item id=$id")
        else Log.d(TAG, "getItemById: trobat item id=$id, tipus=${item.type}")
        return item
    }

    fun addItem(tripId: Int, ruta: String, form: PlanFormState) : Int {
        Log.d(TAG, "addItem: intent d'afegir item -> tripId=$tripId, ruta=$ruta")

        val planType = PlanType.entries.find { it.route == ruta }
            ?: run {
                Log.e(TAG, "addItem: ruta desconeguda -> '$ruta'")
                return AppError.UNKNOWN.code
            }
        val parsedDate = parseDate(form.checkInDate)
            ?: run {
                Log.w(TAG, "addItem: format de data invàlid -> '${form.checkInDate}'")
                return AppError.NON_EXISTING_DATE.code
            }

        val validationStatus = validateDate(parsedDate, tripId)
        if (validationStatus != AppError.OK.code) {
            Log.w(TAG, "addItem: data fora del rang del viatge -> $parsedDate (tripId=$tripId)")
            return validationStatus
        }

        val newItem = buildItineraryItem(tripId = tripId, ruta = ruta, planType = planType, form = form, date = parsedDate)
        val result = repository.addItineraryItem(newItem)
        Log.i(TAG, "addItem: item creat correctament -> tripId=$tripId, tipus=$planType, data=$parsedDate")
        return result
    }

    fun updateItem(itemId: Int, ruta: String, form: PlanFormState): Int {
        Log.d(TAG, "updateItem: intent d'actualitzar item id=$itemId, ruta=$ruta")

        val existing = repository.getItemById(itemId)
            ?: run {
                Log.w(TAG, "updateItem: item no trobat -> id=$itemId")
                return AppError.NON_EXISTING_ITEM.code
            }

        val parsedDate = parseDate(form.checkInDate)
            ?: run {
                Log.w(TAG, "updateItem: format de data invàlid -> '${form.checkInDate}'")
                return AppError.NON_EXISTING_DATE.code
            }

        val validationStatus = validateDate(parsedDate, existing.tripId)
        if (validationStatus != AppError.OK.code) {
            Log.w(TAG, "updateItem: data fora del rang del viatge -> $parsedDate (tripId=${existing.tripId})")
            return validationStatus
        }

        val updated = if (ruta in transports) {
            existing.copy(
                price = form.price, origin = form.locationName, destination = form.destination,
                company = form.company, transportNumber = form.transportNumber, departureDate = parsedDate
            )
        } else {
            existing.copy(price = form.price, locationName = form.locationName, address = form.address, checkInDate = parsedDate)
        }

        val result = repository.updateItineraryItem(updated)
        Log.i(TAG, "updateItem: item actualitzat correctament -> id=$itemId, tipus=${existing.type}")
        return result
    }

    fun deleteItem(item: ItineraryItem): Int {
        Log.d(TAG, "deleteItem: intent d'eliminar item id=${item.id}, tipus=${item.type}")
        val result = repository.deleteItineraryItem(item.id)
        if (result == AppError.OK.code) {
            Log.i(TAG, "deleteItem: item eliminat correctament -> id=${item.id}")
        } else {
            Log.w(TAG, "deleteItem: no s'ha pogut eliminar item id=${item.id}")
        }
        return result
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