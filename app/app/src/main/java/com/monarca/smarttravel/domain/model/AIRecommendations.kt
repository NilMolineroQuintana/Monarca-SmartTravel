package com.monarca.smarttravel.domain.model

import java.util.Date

/**
 * Representa les recomanacions generades per la intel·ligència artificial per a un viatge.
 *
 * Aquesta classe de dades emmagatzema la informació relacionada amb les recomanacions
 * que l'IA genera a partir d'un prompt i les preferències de l'usuari.
 *
 * @property id Identificador únic de la recomanació.
 * @property tripId Identificador del viatge associat a aquesta recomanació.
 * @property type Tipus de recomanació (per exemple: "allotjament", "restauració", "activitats").
 * @property prompt Text d'entrada utilitzat per generar la recomanació.
 * @property response Resposta generada per la IA.
 * @property generatedAt Data i hora en què es va generar la recomanació. Per defecte, la data actual.
 */
data class AIRecommendations(
    val id: String,
    val tripId: String,
    val type: String,
    val prompt: String,
    val response: String,
    val generatedAt: Date = Date()
) {

    /**
     * Genera recomanacions personalitzades basades en la ubicació i les preferències de l'usuari.
     *
     * @param location Ubicació actual o destí del viatge per al qual es generen les recomanacions.
     * @param preferences Llista de preferències de l'usuari (per exemple: ["vegetarià", "econòmic", "cultural"]).
     */
    fun generateRecommendations(location: String, preferences: List<String>) {
        // @TODO Implementar la generació de recomanacions amb IA
    }

    /**
     * Actualitza les recomanacions existents tornant a consultar la IA.
     *
     * Útil quan l'usuari vol obtenir noves suggerències per al mateix viatge
     * sense crear una nova entrada de recomanació.
     */
    fun refreshRecommendations() {
        // @TODO Implementar l'actualització de recomanacions
    }
}