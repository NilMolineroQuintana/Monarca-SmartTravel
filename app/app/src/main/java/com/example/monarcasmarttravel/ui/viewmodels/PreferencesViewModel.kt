package com.example.monarcasmarttravel.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.monarcasmarttravel.data.repository.PreferencesManager
import com.example.monarcasmarttravel.ui.theme.ThemeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val sharedPrefsManager: PreferencesManager
) : ViewModel() {

    var username by mutableStateOf(sharedPrefsManager.username)
        private set

    var dateOfBirth by mutableStateOf(sharedPrefsManager.dateOfBirth)
        private set

    var language by mutableStateOf(sharedPrefsManager.language)
        private set

    var isDarkMode by mutableStateOf(sharedPrefsManager.isDarkMode)
        private set

    var notifications by mutableStateOf(sharedPrefsManager.notifications)
        private set

    fun updateUsername(value: String) { sharedPrefsManager.username = value; username = value }
    fun updateDateOfBirth(value: String) { sharedPrefsManager.dateOfBirth = value; dateOfBirth = value }
    fun updateLanguage(value: String) { sharedPrefsManager.language = value; language = value }
    fun updateDarkMode(value: Boolean) { sharedPrefsManager.isDarkMode = value; isDarkMode = value; ThemeState.isDarkMode = value }
    fun updateNotifications(value: Boolean) { sharedPrefsManager.notifications = value; notifications = value }
}