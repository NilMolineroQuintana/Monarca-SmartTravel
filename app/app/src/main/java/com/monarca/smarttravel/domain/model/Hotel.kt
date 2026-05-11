package com.monarca.smarttravel.domain.model

import okhttp3.Address

data class HotelResponse(
    val available_hotels: List<Hotel>
)

data class Hotel(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val image_url: String,
    val rooms: List<Room>
)

data class Room(
    val id: String,
    val room_type: String,
    val price: Double,
    val images: List<String>
)