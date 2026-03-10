package com.example.monarcasmarttravel

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class LanguageChangeUtil {
    fun changeLanguage(context: Context, languageCode: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(languageCode)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        }
    }

    fun getLanguageCode(context: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val locales = context.getSystemService(LocaleManager::class.java).applicationLocales
            if (!locales.isEmpty) {
                locales[0].language
            } else {
                context.resources.configuration.locales[0].language
            }
        } else {
            val locales = AppCompatDelegate.getApplicationLocales()
            if (!locales.isEmpty) {
                locales[0]?.language ?: "en"
            } else {
                context.resources.configuration.locales[0].language
            }
        }
    }
}