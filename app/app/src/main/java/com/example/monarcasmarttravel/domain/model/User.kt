package com.example.monarcasmarttravel.domain.model

/**
 * Model de dades que representa un usuari de l'aplicació.
 *
 * @param userId Identificador únic de l'usuari.
 * @param name Nom visible de l'usuari.
 * @param email Adreça de correu electrònic, usada per a l'autenticació.
 */
data class User(
    val userId: String,
    val name: String,
    val email: String,
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