package com.example.monarcasmarttravel.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monarcasmarttravel.domain.interfaces.ItineraryItemRepository
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.ui.screens.trip.PlanType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel  // ← Hilt gestiona la creación del ViewModel
class ItineraryItemViewModel @Inject constructor(
    private val repository: ItineraryItemRepository
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

    fun addItem(
        tripId: Int,
        ruta: String,
        locationName: String,
        destination: String,
        company: String,
        transportNumber: String,
        address: String,
        price: String,
        checkInDate: String
    ) : Boolean {
        val transports = listOf("train", "boat", "flight")
        val planType = PlanType.entries.find { it.route == ruta } ?: return false

        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val defaultDate = dateTimeFormat.parse("23/03/2026 10:00")
        val parsedDate = runCatching { dateTimeFormat.parse(checkInDate) }.getOrNull() ?: defaultDate

        val newItem = if (ruta in transports) {
            ItineraryItem(
                id = 0,
                tripId = tripId,
                type = planType,
                price = price.toDoubleOrNull() ?: 0.0,
                origin = locationName,
                destination = destination,
                company = company,
                transportNumber = transportNumber,
                departureDate = parsedDate
            )
        } else {
            ItineraryItem(
                id = 0,
                tripId = tripId,
                type = planType,
                price = price.toDoubleOrNull() ?: 0.0,
                locationName = locationName,
                address = address,
                checkInDate = parsedDate
            )
        }

        return repository.addItineraryItem(newItem)
    }

    fun updateItem(item: ItineraryItem) : Boolean {
        return repository.updateItineraryItem(item)
    }

    fun deleteItem(item: ItineraryItem) : Boolean {
        return repository.deleteItineraryItem(item.id)
    }
}