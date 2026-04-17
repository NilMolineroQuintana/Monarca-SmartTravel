package com.example.monarcasmarttravel.data.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.example.monarcasmarttravel.data.UserDao
import com.example.monarcasmarttravel.domain.interfaces.AuthRepository
import com.example.monarcasmarttravel.domain.model.User
import com.example.monarcasmarttravel.utils.AppError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val userDao: UserDao
): AuthRepository {

    override suspend fun login(email: String, password: String): AppError {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return AppError.UNKNOWN
            userDao.getUserById(uid) ?: return AppError.MISSING_FIELDS
            userDao.registerAccess(uid)
            AppError.OK
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AppError.INVALID_CREDENTIALS
        } catch (e: Exception) {
            AppError.UNKNOWN
        }
    }

    override suspend fun registerUser(user: User, isCompleting: Boolean): AppError {
        return try {
            Log.d("AuthRepositoryImpl", "before registerUser: $user")
            var uid = ""
            if (!isCompleting) {
                val existingUser =  userDao.getUserByUsername(user.username)
                if (existingUser != null) return AppError.EXISTING_USERNAME

                val result = auth.createUserWithEmailAndPassword(user.email, user.password).await()
                uid = result.user?.uid ?: return AppError.UNKNOWN
            } else {
                uid = auth.currentUser?.uid ?: return AppError.UNKNOWN
            }
            userDao.insertUser(user.copy(userId = uid, email = "", password = ""))
            userDao.registerAccess(uid)
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

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout(): Boolean {
        auth.signOut()
        return true
    }

    override fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun getUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return userDao.getUserById(uid)
    }

    override suspend fun updateUser(user: User): AppError {
        return try {
            userDao.updateUser(user)
            AppError.OK
        } catch (e: SQLiteConstraintException) {
            AppError.EXISTING_USERNAME
        }
        catch (e: Exception) {
            AppError.UNKNOWN
        }
    }
}