package com.monarca.smarttravel.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ThemeState {
    var isDarkMode: Boolean by mutableStateOf(false)
}