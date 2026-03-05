package com.example.monarcasmarttravel.domain

import java.util.Date

data class Image(
    val id: Int,
    val tripId: Int,
    val imageId: Int,
    val dateUploaded: Date,
) {
    fun uploadImage() {
        // @TODO Implement upload image
    }

    fun deleteImage() {
        // @TODO Implement delete image
    }
}