package com.example.monarcasmarttravel.domain.model

import com.example.monarcasmarttravel.domain.interfaces.TripRepository
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
     * Afegeix aquest viatge al sistema a través del [TripRepository].
     * @return El viatge creat amb l'ID assignat pel repositori.
     */
    fun addTrip(repository: TripRepository): Trip {
        return repository.addTrip(this)
    }

    /**
     * Actualitza la imatge d'aquest viatge a través del [TripRepository].
     * @param newImageResId Nou recurs drawable, o null per treure la imatge.
     * @return El viatge actualitzat, o null si no s'ha trobat.
     */
    fun editTrip(repository: TripRepository, newImageResId: Int?): Trip? {
        return repository.updateImage(this.id, newImageResId)
    }

    /**
     * Elimina aquest viatge del sistema a través del [TripRepository].
     * @return true si s'ha eliminat correctament, false si no s'ha trobat.
     */
    fun deleteTrip(repository: TripRepository): Boolean {
        return repository.deleteTrip(this.id)
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