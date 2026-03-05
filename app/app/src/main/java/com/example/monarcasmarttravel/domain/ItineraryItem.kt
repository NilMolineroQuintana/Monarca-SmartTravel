package com.example.monarcasmarttravel.domain

import com.example.monarcasmarttravel.ui.screens.PlanType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ItineraryItem(
    val id: Int,
    val tripId: Int,
    val type: PlanType,
    val price: Double,

    // Transport (FLIGHT, BOAT, TRAIN)
    val origin: String? = null,
    val destination: String? = null,   // <- falta en tu UI actual
    val company: String? = null,
    val transportNumber: String? = null,
    val departureDate: Date? = null,

    // Allotjament / POI (HOTEL, RESTAURANT, LOCATION, PARKING)
    val locationName: String? = null,
    val address: String? = null,
    val checkInDate: Date? = null,
    val checkOutDate: Date? = null,    // Només HOTEL i PARKING
) {

    fun addItineraryItem() {
        // @TODO Implement add itinerary item
    }

    fun deleteItineraryItem() {
        // @TODO Implement delete itinerary item
    }

    fun getDate(): Date? = checkInDate ?: departureDate

    fun getCheckInTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(getDate())
    }

    fun formatDateKey(date: Date): String {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        return sdf.format(date)
    }
}