package com.example.monarcasmarttravel.data.repository

import android.util.Log
import com.example.monarcasmarttravel.data.UserDao
import com.example.monarcasmarttravel.domain.interfaces.AuthRepository
import com.example.monarcasmarttravel.domain.model.User
import com.example.monarcasmarttravel.utils.AppError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val userDao: UserDao
): AuthRepository {
    override fun loginUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(user: User): AppError {
        return try {
            Log.d("AuthRepositoryImpl", "before registerUser: $user")
            val existingUser =  userDao.getUserByUsername(user.username)
            Log.d("AuthRepositoryImpl", "registerUser: $existingUser")
            if (existingUser != null) return AppError.EXISTING_USERNAME

            val result = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            val uid = result.user?.uid ?: return AppError.UNKNOWN

            userDao.insertUser(user.copy(userId = uid))
            AppError.OK
        } catch (e: FirebaseAuthUserCollisionException) {
            AppError.EXISTING_EMAIL
        } catch (e: FirebaseAuthWeakPasswordException) {
            AppError.REQUIREMENTS_PASSWORD
        }
        catch (e: Exception) {
            AppError.UNKNOWN
        }
    }

    override fun isAuth() {
        TODO("Not yet implemented")
    }
}