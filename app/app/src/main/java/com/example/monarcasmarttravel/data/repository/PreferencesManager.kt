package com.example.monarcasmarttravel.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.monarcasmarttravel.utils.LanguageChangeUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val preferences: SharedPreferences,
    private val context: Context
) {

    val languageChangeUtil by lazy {
        LanguageChangeUtil()
    }

    var username: String
        get() = preferences.getString("username", "") ?: ""
        set(value) { preferences.edit().putString("username", value).commit() }

    var dateOfBirth: String
        get() = preferences.getString("dateOfBirth", "") ?: ""
        set(value) { preferences.edit().putString("dateOfBirth", value).commit() }

    var isDarkMode: Boolean
        get() = preferences.getBoolean("darkMode", true)
        set(value) { preferences.edit().putBoolean("darkMode", value).commit() }

    var language: String
        get() {
            val saved = preferences.getString("language", null)
            if (saved != null) return saved

            val validLanguages = listOf("ca", "es", "en")
            val deviceLanguage = context.resources.configuration.locales[0].language
            val defaultLanguage = if (deviceLanguage in validLanguages) deviceLanguage else "en"

            preferences.edit().putString("language", defaultLanguage).commit()

            languageChangeUtil.changeLanguage(context, defaultLanguage)
            return defaultLanguage
        }
        set(value) {
            preferences.edit().putString("language", value).commit()
            languageChangeUtil.changeLanguage(context, value)
        }

    var notifications: Boolean
        get() = preferences.getBoolean("notifications", true)
        set(value) { preferences.edit().putBoolean("notifications", value).commit() }
}