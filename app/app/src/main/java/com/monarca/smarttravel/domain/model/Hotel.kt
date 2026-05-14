package com.monarca.smarttravel.domain.model

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

data class BookingData(
    val id: String? = null,
    val hotel_id: String,
    val room_id: String,
    val start_date: String,
    val end_date: String,
    val guest_name: String,
    val guest_email: String
)

data class BookingResponse(
    val message: String,
    val nights: Int,
    val reservation: BookingData
)