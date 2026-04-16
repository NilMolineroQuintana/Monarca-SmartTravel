package com.example.monarcasmarttravel.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monarcasmarttravel.domain.interfaces.AuthRepository
import com.example.monarcasmarttravel.domain.model.User
import com.example.monarcasmarttravel.utils.AppError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val error: AppError) : RegisterState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun registerUser(username: String, date: String, email: String, phoneNum: String, address: String, password: String) {
        val user = User(username = username, birthdate = date, email = email, phoneNum = phoneNum, address = address, password = password)

        viewModelScope.launch {
            Log.d("AuthViewModel", "registerUser: $user")
            _registerState.value = RegisterState.Loading

            val result = repository.registerUser(user)

            _registerState.value = when (result) {
                AppError.OK -> RegisterState.Success
                else -> RegisterState.Error(result)
            }
        }
    }
}