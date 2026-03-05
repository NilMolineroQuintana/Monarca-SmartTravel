package com.example.monarcasmarttravel.domain

data class Preferences(
    val userId: String,
    val notificationEnabled: Boolean,
    val preferredLanguage: String,
    val themeDark: Boolean
) {
    fun updatePreferences() {
        // @TODO Implement update preferences
    }
}