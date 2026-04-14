package com.example.monarcasmarttravel.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monarcasmarttravel.domain.interfaces.UserRepository
import com.example.monarcasmarttravel.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    fun registerUser(username: String, birthdayDate: String, email: String, phoneNum: String, address: String, password: String) {
        if (username.isEmpty() || birthdayDate.isEmpty() || email.isEmpty() || phoneNum.isEmpty() || address.isEmpty() || password.isEmpty()) {
            return
        }
        viewModelScope.launch {
            try {
                repository.insertUser(User(0,username, birthdayDate, email, phoneNum, address, password))
                Log.d("UserViewModel", "User registered successfully")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error registering user", e)
            }
        }
    }
}