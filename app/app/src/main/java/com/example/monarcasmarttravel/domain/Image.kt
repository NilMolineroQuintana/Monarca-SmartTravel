package com.example.monarcasmarttravel.domain

import java.util.Date

data class Image(
    val id: Int,
    val tripId: Int,
    val imageId: Int,
    val dateUploaded: Date,
)


// No afegim funcions que agreguen ja que d'això s'encarregarà un Repository