package com.example.monarcasmarttravel.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.domain.interfaces.ItineraryItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    fun addItem(item: ItineraryItem) {
        viewModelScope.launch {
            val success = repository.addItineraryItem(item)
            if (success) loadItemsByTrip(item.tripId)
        }
    }

    fun updateItem(item: ItineraryItem) {
        viewModelScope.launch {
            val success = repository.updateItineraryItem(item)
            if (success) loadItemsByTrip(item.tripId)
        }
    }

    fun deleteItem(item: ItineraryItem) {
        viewModelScope.launch {
            val success = repository.deleteItineraryItem(item.id)
            if (success) loadItemsByTrip(item.tripId)
        }
    }
}