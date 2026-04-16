package com.example.monarcasmarttravel.data.repository

import com.example.monarcasmarttravel.domain.interfaces.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): AuthRepository {
    override fun loginUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun registerUser(
        username: String,
        birthdayDate: String,
        email: String,
        phoneNum: String,
        address: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun isAuth() {
        TODO("Not yet implemented")
    }
}