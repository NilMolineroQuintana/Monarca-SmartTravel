package com.example.monarcasmarttravel.domain.interfaces

import com.example.monarcasmarttravel.domain.model.User
import com.example.monarcasmarttravel.utils.AppError

interface AuthRepository {
    suspend fun registerUser(user: User): AppError
    suspend fun login(email: String, password: String): Result<Boolean>
    suspend fun getUser(): User?
    suspend fun updateUser(user: User): AppError
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    fun logout(): Boolean
    fun isLoggedIn(): Boolean
}