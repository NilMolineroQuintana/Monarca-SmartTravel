package com.example.monarcasmarttravel.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.monarcasmarttravel.R
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
    val destination: String? = null,
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

    fun updateItem() {
        // @TODO Updates the item when the time has arrived
    }


    fun hasScheduleConflict(other: ItineraryItem): Boolean {
        // @TODO Logic to compare departureDate or checkInDate between items
        return false
    }

    fun getInDate(): Date? = checkInDate ?: departureDate

    @Composable
    fun getDateInTime(): String {
        val date = getInDate() ?: return stringResource(R.string.no_date)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    @Composable
    fun getDateOutTime(): String {
        val date = checkOutDate ?: return stringResource(R.string.no_date)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    fun formatDateKey(date: Date): String {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        return sdf.format(date)
    }
}