package com.example.monarcasmarttravel.data.repository

import android.content.Context
import android.content.SharedPreferences

class PreferencesRepository(context: Context) {

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
        get() = preferences.getString("language", "ca") ?: "ca"
        set(value) = preferences.edit().putString("language", value).apply()

    var notifications: Boolean
        get() = preferences.getBoolean("notifications", true)
        set(value) = preferences.edit().putBoolean("notifications", value).apply()
}