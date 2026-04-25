package com.monarca.smarttravel.domain.interfaces

import com.monarca.smarttravel.domain.model.User
import com.monarca.smarttravel.utils.AppError

interface AuthRepository {
    suspend fun registerUser(user: User, isCompleting: Boolean): AppError
    suspend fun login(email: String, password: String): AppError
    suspend fun getUser(): User?
    suspend fun updateUser(user: User): AppError
    suspend fun sendPasswordResetEmail(email: String): AppError
    suspend fun logout(): Boolean
    fun isLoggedIn(): Boolean
    suspend fun isEmailVerified(): AppError
}