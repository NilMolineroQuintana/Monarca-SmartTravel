package com.example.monarcasmarttravel.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Model de dades que representa un usuari de l'aplicació.
 *
 * @param userId Identificador únic de l'usuari.
 * @param name Nom visible de l'usuari.
 * @param email Adreça de correu electrònic, usada per a l'autenticació.
 */

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["username"], unique = true),
        Index(value = ["email"], unique = true)
    ]
)
data class User(
    @PrimaryKey val userId: String = "",
    val username: String,
    val birthdate: String,
    val email: String,
    val phoneNum: String,
    val address: String,
    val password: String
) {
    /**
     * Registra un nou usuari al sistema.
     * Pendent d'implementar.
     */
    fun addUser(): Unit {
        // @TODO Implement user creation
    }

    /**
     * Elimina el compte de l'usuari del sistema.
     * Pendent d'implementar.
     */
    fun deleteUser(): Unit {
        // @TODO Implement user deletion
    }

    /**
     * Actualitza les dades del perfil de l'usuari (nom, correu, etc.).
     * Pendent d'implementar.
     */
    fun updateUser(): Unit {
        // @TODO Implement update user info
    }
}