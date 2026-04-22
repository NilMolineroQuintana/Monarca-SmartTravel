package com.monarca.smarttravel.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.monarca.smarttravel.data.repository.PreferencesManager
import com.monarca.smarttravel.ui.theme.ThemeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val sharedPrefsManager: PreferencesManager
) : ViewModel() {

    var language by mutableStateOf(sharedPrefsManager.language)
        private set

    var isDarkMode by mutableStateOf(sharedPrefsManager.isDarkMode)
        private set

    var notifications by mutableStateOf(sharedPrefsManager.notifications)
        private set

    fun updateLanguage(value: String) { sharedPrefsManager.language = value; language = value }
    fun updateDarkMode(value: Boolean) { sharedPrefsManager.isDarkMode = value; isDarkMode = value; ThemeState.isDarkMode = value }
    fun updateNotifications(value: Boolean) { sharedPrefsManager.notifications = value; notifications = value }
}