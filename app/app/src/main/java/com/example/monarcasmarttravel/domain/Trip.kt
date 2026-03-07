package com.example.monarcasmarttravel.domain

import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Model de dades que representa un viatge planificat per l'usuari.
 *
 * @param id Identificador únic del viatge.
 * @param destination Destinació principal del viatge (p. ex., "Kyoto, Japó").
 * @param dateIn Data d'inici del viatge.
 * @param dateOut Data de finalització del viatge.
 * @param userId Identificador únic del propietari del viatge.
 */
data class Trip(
    val id: Int,
    val destination: String,
    val dateIn: Date,
    val dateOut: Date,
    val imageResId: Int? = null,
    val userId: Int
) {
    /**
     * Crea un nou viatge al sistema
     * Pendent d'implementar
     */
    fun createTrip() {
        // @TODO Implement create trip
    }

    /**
     * Elimina un viatge del sistema
     * Pendent d'implementar
     */
    fun deleteTrip() {
        // @TODO Implement delete trip
    }

    /**
     * Actualitza un viatge existent del sistema
     * Pendent d'implementar
     */
    fun updateTrip() {
        // @TODO Implement update trip
    }

    /**
     * Calcula la durada total del viatge en dies.
     */
    fun getTripDuration(): Long {
        val diffInMillis = dateOut.time - dateIn.time
        return TimeUnit.MILLISECONDS.toDays(diffInMillis) + 1
    }

    /**
     * Calcula els dies que falten fins a l'inici del viatge.
     */
    fun getDaysUntilStart(): Long {
        return TimeUnit.MILLISECONDS.toDays(dateIn.time - System.currentTimeMillis())
    }
}