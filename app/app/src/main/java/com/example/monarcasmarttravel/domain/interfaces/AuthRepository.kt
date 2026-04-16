package com.example.monarcasmarttravel.domain.interfaces

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Boolean>
    suspend fun register(email: String, password: String, username: String, birthdate: String, phoneNum: String, address: String): Result<Boolean>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    fun logout(): Boolean
    fun isLoggedIn(): Boolean
}