package com.example.monarcasmarttravel.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBoatFilled
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Train
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.monarcasmarttravel.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
enum class PlanType(
    val titleRes: Int,
    val icon: ImageVector,
    val route: String,
    val backgroundColor: Color,
    val iconColor: Color
) {
    FLIGHT(
        R.string.plan_flight,
        Icons.Default.FlightTakeoff,
        "flight",
        Color(0xFFB3E5FC),
        Color(0xFF0277BD)
    ),
    BOAT(
        R.string.plan_boat,
        Icons.Default.DirectionsBoatFilled,
        "boat",
        Color(0xFFB2DFDB),
        Color(0xFF00695C)
    ),
    TRAIN(
        R.string.plan_train,
        Icons.Default.Train,
        "train",
        Color(0xFFE1BEE7),
        Color(0xFF6A1B9A)
    ),
    HOTEL(
        R.string.plan_hotel,
        Icons.Default.Hotel,
        "hotel",
        Color(0xFFFFCCBC),
        Color(0xFFD84315)
    ),
    RESTAURANT(
        R.string.plan_restaurant,
        Icons.Default.Restaurant,
        "restaurant",
        Color(0xFFC8E6C9),
        Color(0xFF2E7D32)
    ),
    LOCATION(
        R.string.plan_location,
        Icons.Default.LocationCity,
        "location",
        Color(0xFFFFF9C4),
        Color(0xFFF9A825)
    )
}

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
}