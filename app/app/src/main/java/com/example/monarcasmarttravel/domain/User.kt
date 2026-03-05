package com.example.monarcasmarttravel.domain

data class User(
    val userId: String,
    val name: String,
    val email: String,
    val password: String
)

// No afegim funcions que agreguen ja que d'això s'encarregarà un Repository