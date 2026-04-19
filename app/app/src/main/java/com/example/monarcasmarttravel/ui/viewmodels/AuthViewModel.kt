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

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object RecoverEmailSent : AuthState()
    data class Error(val error: AppError) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        viewModelScope.launch {
            _user.value = repository.getUser()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(email, password)
            if (result == AppError.OK) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error(result)
            }
        }
    }


    fun registerUser(username: String, date: String, email: String, phoneNum: String, address: String, password: String, isCompleting: Boolean) {
        val user = User(username = username, birthdate = date, email = email, phoneNum = phoneNum, address = address, password = password)

        viewModelScope.launch {
            Log.d("AuthViewModel", "registerUser: $user")
            _registerState.value = RegisterState.Loading

            val result = repository.registerUser(user, isCompleting)

            if (result == AppError.OK) {
                _user.value = repository.getUser()
                _registerState.value = RegisterState.Success
            } else {
                _registerState.value = RegisterState.Error(result)
            }
        }
    }

    fun updateUser(user: User, onResult: (AppError) -> Unit) {
        viewModelScope.launch {
            val result = repository.updateUser(user)
            if (result == AppError.OK) {
                _user.value = repository.getUser()
            }
            onResult(result)
        }
    }
/*
    fun recoverPassword(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.sendPasswordResetEmail(email)
            result.onSuccess {
                _authState.value = AuthState.RecoverEmailSent
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Error al enviar el correu de recuperació")
            }
        }
    }
*/
    fun logout() {
        repository.logout()
        _authState.value = AuthState.Idle
    }

    fun isLoggedIn(): Boolean {
        return repository.isLoggedIn()
    }

    fun isEmailVerified() {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val result = repository.isEmailVerified()
            _registerState.value = if (result == AppError.OK) {
                RegisterState.Success
            } else {
                RegisterState.Error(result)
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
        _authState.value = AuthState.Idle
    }
}