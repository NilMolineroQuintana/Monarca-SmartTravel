package com.monarca.smarttravel.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Model de dades que representa una imatge associada a un viatge concret.
 *
 * @param id Identificador únic de la imatge.
 * @param tripId Identificador del viatge al qual pertany la imatge.
 * @param imagePath Referència a la imatge.
 * @param dateUploaded Data en què es va pujar la imatge.
 */
@Entity(
    tableName = "images",
    indices = [
        Index(value = ["tripId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int,
    val imagePath: String,
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