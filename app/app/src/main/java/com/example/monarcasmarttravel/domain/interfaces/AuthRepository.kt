package com.example.monarcasmarttravel.domain.interfaces

import com.example.monarcasmarttravel.domain.model.User

interface AuthRepository {
    fun loginUser(email: String, password: String, onComplete: (Boolean) -> Unit)
    fun registerUser(user: User, onComplete: (Boolean) -> Unit)
    fun isAuth()
}