package com.example.monarcasmarttravel.domain

import com.example.monarcasmarttravel.ui.screens.PlanType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ItineraryItem (
    val id: Int,
    val type: PlanType,
    val price: Double,
    val checkInDate: Date,
    val checkOutDate: Date? = null,
    val locationName: String = "",
    val address: String = "",
    val company: String = "",
    val transportNumber: String = ""
) {
    fun getCheckInTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(checkInDate)
    }

    fun getCheckOutTime(): String? {
        if (checkOutDate == null) return null

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(checkOutDate)
    }

    fun formatDateKey(date: Date): String {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        return sdf.format(date)
    }

    fun addItineraryItem() {
        // @TODO Implement add itinerary item
    }

    fun deleteItineraryItem() {
        // @TODO Implement delete itinerary item
    }
}