package com.example.monarcasmarttravel.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monarcasmarttravel.domain.interfaces.ItineraryRepository
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.ui.screens.trip.PlanFormState
import com.example.monarcasmarttravel.ui.screens.trip.PlanType
import com.example.monarcasmarttravel.utils.AppError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel  // ← Hilt gestiona la creación del ViewModel
class ItineraryViewModel @Inject constructor(
    private val repository: ItineraryRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<ItineraryItem>>(emptyList())
    val items: StateFlow<List<ItineraryItem>> = _items

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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
        val transports = listOf("train", "boat", "flight")
        val planType = PlanType.entries.find { it.route == ruta } ?: return AppError.UNKNOWN.code

        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val defaultDate = dateTimeFormat.parse("23/03/2026 10:00")
        val parsedDate = runCatching { dateTimeFormat.parse(form.checkInDate) }.getOrNull() ?: defaultDate

        val newItem = if (ruta in transports) {
            ItineraryItem(
                id = 0,
                tripId = tripId,
                type = planType,
                price = form.price,
                origin = form.locationName,
                destination = form.destination,
                company = form.company,
                transportNumber = form.transportNumber,
                departureDate = parsedDate
            )
        } else {
            ItineraryItem(
                id = 0,
                tripId = tripId,
                type = planType,
                price = form.price,
                locationName = form.locationName,
                address = form.address,
                checkInDate = parsedDate
            )
        }

        return repository.addItineraryItem(newItem)
    }

    fun updateItem(itemId: Int, ruta: String, form: PlanFormState): Int {
        val existing = repository.getItemById(itemId) ?: return AppError.ITEM_NOT_FOUND.code

        val transports = listOf("train", "boat", "flight")
        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val parsedDate = runCatching { dateTimeFormat.parse(form.checkInDate) }.getOrNull()
            ?: existing.getInDate()

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
}