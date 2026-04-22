package com.monarca.smarttravel.domain.model

import java.util.Date

/**
 * Model de dades que representa una imatge associada a un viatge concret.
 *
 * @param id Identificador únic de la imatge.
 * @param tripId Identificador del viatge al qual pertany la imatge.
 * @param imageId Referència al recurs drawable de la imatge (R.drawable.*).
 * @param dateUploaded Data en què es va pujar la imatge.
 */
data class Image(
    val id: Int,
    val tripId: Int,
    val imageId: Int,
    val dateUploaded: Date,
) {
    /**
     * Puja la imatge al servidor o emmagatzematge en núvol.
     * Pendent d'implementar.
     */
    fun uploadImage() {
        // @TODO Implement upload image
    }

    /**
     * Elimina la imatge de l'àlbum del viatge.
     * Pendent d'implementar.
     */
    fun deleteImage() {
        // @TODO Implement delete image
    }
}