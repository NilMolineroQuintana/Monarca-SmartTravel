package com.monarca.smarttravel.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monarca.smarttravel.domain.interfaces.BookingRepository
import com.monarca.smarttravel.domain.model.BookingData
import com.monarca.smarttravel.domain.model.Hotel
import com.monarca.smarttravel.domain.model.Room
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

    private val _hotels = MutableStateFlow<List<Hotel>?>(emptyList())
    val hotels = _hotels.asStateFlow()

    private val _selectedHotel = MutableStateFlow<Hotel?>(null)
    val selectedHotel = _selectedHotel.asStateFlow()

    private val _selectedRoom = MutableStateFlow<Room?>(null)
    val selectedRoom = _selectedRoom.asStateFlow()

    private val _startDate = MutableStateFlow("")
    val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow("")
    val endDate = _endDate.asStateFlow()

    private val _uiState = MutableStateFlow<BookingUiState>(BookingUiState.Idle)
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    fun resetState() { _uiState.value = BookingUiState.Idle }

    fun selectHotel(hotel: Hotel) {
        _selectedHotel.value = hotel
    }

    fun selectRoom(room: Room) {
        _selectedRoom.value = room
    }

    fun getAvailable(start_date: String, end_date: String, city: String) {
        _startDate.value = start_date
        _endDate.value = end_date
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

    fun bookRoom(guestName: String, email: String) {
        Log.d("BookingViewModel", "Iniciant reserva per a: $guestName, $email")
        viewModelScope.launch {
            _uiState.value = BookingUiState.Loading
            try {
                val hotelId = _selectedHotel.value?.id ?: throw IllegalStateException("Hotel ID is missing")
                val roomId = _selectedRoom.value?.id ?: throw IllegalStateException("Room ID is missing")
                val startDate = _startDate.value.takeIf { it.isNotEmpty() } ?: throw IllegalStateException("Start date is missing")
                val endDate = _endDate.value.takeIf { it.isNotEmpty() } ?: throw IllegalStateException("End date is missing")

                val request = BookingData(
                    hotel_id = hotelId,
                    room_id = roomId,
                    start_date = startDate,
                    end_date = endDate,
                    guest_name = guestName,
                    guest_email = email
                )
                Log.d("BookingViewModel", "Request: $request")
                val response = repository.bookRoom(request)
                Log.d("BookingViewModel", "Response: $response")
                
                if (response != null) {
                    Log.d("BookingViewModel", "Reserva confirmada amb ID: ${response.reservation.id}")
                    _uiState.value = BookingUiState.Success
                } else {
                    _uiState.value = BookingUiState.Error(AppError.UNKNOWN)
                }
            } catch (e: Exception) {
                Log.e("BookingViewModel", "Excepció durant la reserva", e)
                _uiState.value = BookingUiState.Error(AppError.UNKNOWN)
            }
        }
    }
}