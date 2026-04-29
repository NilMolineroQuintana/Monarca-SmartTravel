package com.monarca.smarttravel.domain.model

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
    ]
)
data class User(
    @PrimaryKey val userId: String = "",
    val username: String,
    val birthdate: String,
    val email: String,
    val phoneNum: String,
    val country: String,
    val address: String,
    val password: String,
    val recieveEmails: Boolean
)