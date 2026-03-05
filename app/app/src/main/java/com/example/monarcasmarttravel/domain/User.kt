package com.example.monarcasmarttravel.domain

data class User(
    val userId: String,
    val name: String,
    val email: String,
    val password: String
) {
    fun updateUser(): Unit {
        // @TODO Implement update user info
    }
}