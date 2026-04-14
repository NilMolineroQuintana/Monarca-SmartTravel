package com.example.monarcasmarttravel.domain.interfaces

import com.example.monarcasmarttravel.domain.model.User

interface UserRepository
{
    suspend fun insertUser(user: User)
}