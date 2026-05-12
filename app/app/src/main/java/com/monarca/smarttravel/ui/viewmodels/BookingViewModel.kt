package com.monarca.smarttravel.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monarca.smarttravel.domain.interfaces.BookingRepository
import com.monarca.smarttravel.domain.model.Hotel
import com.monarca.smarttravel.utils.AppError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class BookingUiState {
    object Idle : BookingUiState()
    object Loading : BookingUiState()
    object Success : BookingUiState()
    data class Error(val error: AppError) : BookingUiState()
}

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {

    val _hotels = MutableStateFlow<List<Hotel>?>(emptyList())
    val hotels = _hotels.asStateFlow()

    private val _uiState = MutableStateFlow<BookingUiState>(BookingUiState.Idle)
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    fun resetState() { _uiState.value = BookingUiState.Idle }

    fun getAvailable(start_date: String, end_date: String, city: String) {
        viewModelScope.launch {
            _uiState.value = BookingUiState.Loading
            try {
                _hotels.value = repository.getAvailable(start_date, end_date, city)
                _uiState.value = BookingUiState.Success
            } catch (e: Exception) {
                _uiState.value = BookingUiState.Error(AppError.UNKNOWN)
            }
        }
    }
}