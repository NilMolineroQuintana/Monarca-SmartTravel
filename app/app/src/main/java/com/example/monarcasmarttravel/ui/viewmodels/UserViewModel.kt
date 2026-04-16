package com.example.monarcasmarttravel.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monarcasmarttravel.domain.interfaces.UserRepository
import com.example.monarcasmarttravel.domain.model.User
import com.example.monarcasmarttravel.utils.AppError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var status by mutableStateOf<AppError?>(null)
        private set

    fun clearStatus() {
        status = null
    }

    fun registerUser(username: String, birthdayDate: String, email: String, phoneNum: String, address: String, password: String) {
        if (username.isEmpty() || birthdayDate.isEmpty() || email.isEmpty() || phoneNum.isEmpty() || address.isEmpty() || password.isEmpty()) {
            return
        }
        viewModelScope.launch {
            try {
                repository.insertUser(User(username = username, birthdate =  birthdayDate, email = email, phoneNum = phoneNum, address =  address, password = password))
                Log.d("UserViewModel", "User registered successfully")
                status = AppError.OK
            } catch (e: android.database.sqlite.SQLiteConstraintException) {
                val message = e.message ?: ""

                status = when {
                    message.contains("users.username") -> AppError.EXISTING_USERNAME
                    message.contains("users.email") -> AppError.EXISTING_EMAIL
                    else -> AppError.UNKNOWN
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error registering user", e)
            }
        }
    }
}