package com.example.monarcasmarttravel.domain

data class Preferences(
    val userId: String,
    val notificationEnabled: Boolean,
    val preferredLanguage: String,
    val theme: String
) {
    // No afegim funcions que agreguen ja que d'això s'encarregarà un Repository
}