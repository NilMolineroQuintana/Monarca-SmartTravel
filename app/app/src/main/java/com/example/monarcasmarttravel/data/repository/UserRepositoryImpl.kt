package com.example.monarcasmarttravel.data.repository

import com.example.monarcasmarttravel.data.UserDao
import com.example.monarcasmarttravel.domain.interfaces.UserRepository
import com.example.monarcasmarttravel.domain.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
        return
    }
}