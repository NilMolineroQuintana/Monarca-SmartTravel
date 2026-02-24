package com.example.monarcasmarttravel.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBoatFilled
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Train
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.monarcasmarttravel.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class PlanType(val titleRes: Int, val icon: ImageVector, val route: String) {
    FLIGHT(R.string.plan_flight, Icons.Default.FlightTakeoff, "flight"),
    BOAT(R.string.plan_boat, Icons.Default.DirectionsBoatFilled, "boat"),
    TRAIN(R.string.plan_train, Icons.Default.Train, "train"),
    HOTEL(R.string.plan_hotel, Icons.Default.Hotel, "hotel"),
    RESTAURANT(R.string.plan_restaurant, Icons.Default.Restaurant, "restaurant"),
    LOCATION(R.string.plan_location, Icons.Default.LocationCity, "location"),
    PARKING(R.string.plan_parking, Icons.Default.LocalParking, "parking")
}

data class ItineraryItem (
    val id: Int,
    val type: PlanType,
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
}