package com.example.monarcasmarttravel.domain.interfaces

interface AuthRepository {
    fun loginUser(email: String, password: String, onComplete: (Boolean) -> Unit)
    fun registerUser(username: String, birthdayDate: String, email: String, phoneNum: String, address: String, password: String, onComplete: (Boolean) -> Unit)
    fun isAuth()
}