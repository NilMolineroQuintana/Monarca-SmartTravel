package com.monarca.smarttravel.domain.model

import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Model de dades que representa un viatge planificat per l'usuari.
 *
 * @param id Identificador únic del viatge.
 * @param title Destinació principal del viatge (p. ex., "Kyoto, Japó").
 * @param description Descripció del viatge.
 * @param dateIn Data d'inici del viatge.
 * @param dateOut Data de finalització del viatge.
 * @param imageResId Recurs drawable opcional associat al destí.
 * @param userId Identificador únic del propietari del viatge.
 */
data class Trip(
    val id: Int,
    val title: String,
    val description: String,
    val dateIn: Date,
    val dateOut: Date,
    val imageResId: Int? = null,
    val userId: Int
) {
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

    fun getDaysUntilEnd(): Long {
        return TimeUnit.MILLISECONDS.toDays(dateOut.time - System.currentTimeMillis())
    }
}