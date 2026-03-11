package com.example.monarcasmarttravel.data.repository

import android.content.Context
import android.content.SharedPreferences

class PreferencesRepository(private val context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        "monarca_preferences",
        Context.MODE_PRIVATE
    )

    var username: String
        get() = preferences.getString("username", "") ?: ""
        set(value) = preferences.edit().putString("username", value).apply()

    var dateOfBirth: String
        get() = preferences.getString("dateOfBirth", "") ?: ""
        set(value) = preferences.edit().putString("dateOfBirth", value).apply()

    var isDarkMode: Boolean
        get() = preferences.getBoolean("darkMode", false)
        set(value) = preferences.edit().putBoolean("darkMode", value).apply()

    var language: String
        get() {
            val saved = preferences.getString("language", null)
            if (saved != null) return saved

            val validLanguages = listOf("ca", "es", "en")
            val deviceLanguage = context.resources.configuration.locales[0].language
            val defaultLanguage = if (deviceLanguage in validLanguages) deviceLanguage else "en"

            preferences.edit().putString("language", defaultLanguage).apply()
            return defaultLanguage
        }
        set(value) = preferences.edit().putString("language", value).apply()

    var notifications: Boolean
        get() = preferences.getBoolean("notifications", true)
        set(value) = preferences.edit().putBoolean("notifications", value).apply()
}