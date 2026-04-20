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

    private val TAG = "AuthRepositoryImpl"

    override suspend fun login(email: String, password: String): AppError {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return AppError.UNKNOWN

            if (!result.user!!.isEmailVerified) {
                Log.i(TAG, "Usuari no verificat.")
                return AppError.VERIFICATION_REQUIRED
            }

            val user = userDao.getUserById(uid)
            if (user == null) {
                Log.i(TAG, "Usuari no trobat a la base de dades local.")
                return AppError.MISSING_FIELDS
            }
            userDao.registerAccess(uid)
            Log.i(TAG, "Login correcte. (${uid})")
            AppError.OK
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AppError.INVALID_CREDENTIALS
        } catch (e: Exception) {
            AppError.FIREBASE_UNKNOWN_ERROR
        }
    }

    override suspend fun registerUser(user: User, isCompleting: Boolean): AppError {
        return try {
            Log.d("AuthRepositoryImpl", "before registerUser: $user")
            var uid = ""
            val existingUser =  userDao.getUserByUsername(user.username)
            if (existingUser != null) {
                Log.i(TAG, "Usuari ja existent.")
                return AppError.EXISTING_USERNAME
            }
            if (!isCompleting) {
                val result = auth.createUserWithEmailAndPassword(user.email, user.password).await()
                val firebaseUser = result.user ?: return AppError.UNKNOWN

                firebaseUser.sendEmailVerification().await()
                uid = firebaseUser.uid
                userDao.insertUser(user.copy(userId = uid, email = "", password = ""))
                userDao.registerAccess(uid)
                Log.i(TAG, "Registre correcte enviant a verificació. (${uid})")
                return AppError.VERIFICATION_REQUIRED
            } else {
                uid = auth.currentUser?.uid ?: return AppError.UNKNOWN
            }
            userDao.insertUser(user.copy(userId = uid, email = "", password = ""))
            userDao.registerAccess(uid)
            Log.i(TAG, "Registre correcte. (${uid})")
            AppError.OK
        } catch (e: FirebaseAuthUserCollisionException) {
            AppError.EXISTING_EMAIL
        } catch (e: FirebaseAuthWeakPasswordException) {
            AppError.REQUIREMENTS_PASSWORD
        }
        catch (e: Exception) {
            AppError.FIREBASE_UNKNOWN_ERROR
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): AppError {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AppError.OK
        }
        catch (e: Exception) {
            AppError.FIREBASE_UNKNOWN_ERROR
        }
    }

    override suspend fun logout(eraseUser: User?): Boolean {
        if (eraseUser != null) {
            userDao.deleteUser(eraseUser)
        }
        auth.signOut()
        Log.i(TAG, "Logout correcte.")
        return true
    }

    override fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun isEmailVerified(): AppError {
        return try {
            val user = auth.currentUser ?: return AppError.UNKNOWN

            if (user.isEmailVerified) return AppError.OK

            user.reload().await()
            if (user.isEmailVerified) AppError.OK else AppError.VERIFICATION_REQUIRED
        } catch (e: Exception) {
            AppError.UNKNOWN
        }
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