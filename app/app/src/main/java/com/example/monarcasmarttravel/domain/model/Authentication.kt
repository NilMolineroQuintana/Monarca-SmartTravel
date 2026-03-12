package com.example.monarcasmarttravel.domain.model

/**
 * Model de dades que representa les credencials d'autenticació d'un usuari.
 *
 * Aquesta classe és responsable exclusivament de la seguretat i gestió de sessió.
 * Les dades de perfil (com el correu electrònic) es troben a [User].
 *
 * @param userId Identificador únic de l'usuari al qual pertanyen aquestes credencials.
 * @param password Contrasenya de l'usuari. Mai s'ha d'exposar ni mostrar a la interfície.
 * @param token Token de sessió activa, nul si no hi ha sessió iniciada.
 */
data class Authentication(
    val userId: String,
    val password: String,
    val token: String? = null
) {
    /**
     * Valida les credencials de l'usuari i inicia sessió.
     *
     * El correu electrònic s'obté de [User] i la contrasenya es compara
     * amb la que emmagatzema aquesta classe.
     *
     * @param email Adreça de correu electrònic provinent de [User].
     * @param password Contrasenya introduïda per l'usuari.
     * @return Boolean indicant si l'inici de sessió ha estat exitós.
     */
    fun login(email: String, password: String): Boolean {
        // @TODO Implement authentication login
        return false
    }

    /**
     * Tanca la sessió activa de l'usuari i invalida el [token] actual.
     */
    fun logout() {
        // @TODO Implement logout
    }

    /**
     * Envia un correu electrònic per restablir la contrasenya.
     *
     * @param email Adreça de correu on enviar l'enllaç de restabliment.
     *              S'obté de [User.email], ja que aquesta classe no l'emmagatzema.
     * @return Boolean indicant si l'email s'ha enviat correctament.
     */
    fun resetPassword(email: String): Boolean {
        // @TODO Implement password reset
        return false
    }
}