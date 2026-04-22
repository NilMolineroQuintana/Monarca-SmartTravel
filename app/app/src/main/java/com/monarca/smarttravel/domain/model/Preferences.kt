package com.monarca.smarttravel.domain.model

/**
 * Model de dades que representa les preferències de configuració d'un usuari.
 *
 * @param userId Identificador de l'usuari al qual pertanyen aquestes preferències.
 * @param notificationEnabled Indica si les notificacions push estan activades.
 * @param preferredLanguage Idioma preferit de l'aplicació (p. ex. "Català", "Español").
 * @param theme Tema visual de l'aplicació ("Dark" per al mode fosc, qualsevol altre per al clar).
 */
data class Preferences(
    val userId: String,
    val notificationEnabled: Boolean,
    val preferredLanguage: String,
    val theme: String
) {
    /**
     * Actualitza les preferències de l'usuari al backend o emmagatzematge local.
     * Pendent d'implementar.
     */
    fun updatePreferences() {
        // @TODO Implement update preferences
    }
}