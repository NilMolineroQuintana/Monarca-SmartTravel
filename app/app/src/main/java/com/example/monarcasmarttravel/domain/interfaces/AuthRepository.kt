package com.example.monarcasmarttravel.domain.interfaces

import com.example.monarcasmarttravel.domain.model.User
import com.example.monarcasmarttravel.utils.AppError

interface AuthRepository {
    fun loginUser(email: String, password: String, onComplete: (Boolean) -> Unit)
    suspend fun registerUser(user: User): AppError
    fun isAuth()
}