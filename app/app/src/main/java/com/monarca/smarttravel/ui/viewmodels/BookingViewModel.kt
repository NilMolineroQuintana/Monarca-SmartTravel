package com.monarca.smarttravel.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monarca.smarttravel.domain.interfaces.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {

    fun getAvailable(start_date: String, end_date: String, city: String) {
        viewModelScope.launch {
            repository.getAvailable(start_date, end_date, city)
        }
    }
}