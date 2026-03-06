package com.example.monarcasmarttravel.domain

/**
 * Model de dades que representa l'autenticació d'un usuari.
 *
 * @param email Adreça de correu electrònic de l'usuari (utilitzada com a identificador únic).
 * @param password Contrasenya de l'usuari.
 */
data class Authentication(
    val email: String,
    val password: String
) {
    /**
     * Valida les credencials de l'usuari i inicia sessió.
     *
     * @return Boolean indicant si l'inici de sessió ha estat exitós.
     */
    fun login(): Boolean {
        // @TODO Implement authentication login
        return false
    }

    /**
     * Tanca la sessió activa de l'usuari.
     */
    fun logout() {
        // @TODO Implement logout
    }

    /**
     * Envia un correu electrònic per restablir la contrasenya.
     *
     * @param email Adreça de correu on enviar l'enllaç de restabliment.
     * @return Boolean indicant si l'email s'ha enviat correctament.
     */
    fun resetPassword(email: String): Boolean {
        // @TODO Implement password reset
        return false
    }
}