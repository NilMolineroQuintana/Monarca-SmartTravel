package com.monarca.smarttravel.data.repository

import android.util.Log
import com.monarca.smarttravel.data.remote.HotelAPIService
import com.monarca.smarttravel.domain.interfaces.BookingRepository
import com.monarca.smarttravel.domain.model.BookingData
import com.monarca.smarttravel.domain.model.BookingResponse
import com.monarca.smarttravel.domain.model.Hotel
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val hotelApiService: HotelAPIService
): BookingRepository {
    private val gid = "G12"

    override suspend fun getAvailable(start_date: String, end_date: String, city: String): List<Hotel>? {
        val response = hotelApiService.checkAvailability(gid, start_date, end_date, city)

        if (!response.isSuccessful) return null

        val hotelList = response.body()?.available_hotels

        Log.d("BookingRepositoryImpl", "getAvailable: $hotelList")

        return hotelList
    }

    override suspend fun bookRoom(bookingRequest: BookingData): BookingResponse? {
        val response = hotelApiService.bookRoom(gid, bookingRequest)

        if (!response.isSuccessful) {
            Log.e("BookingRepositoryImpl", "bookRoom error: ${response.errorBody()?.string()}")
            return null
        }

        return response.body()
    }

    override suspend fun cancelReservation(reservationId: String): Boolean {
        return try {
            val response = hotelApiService.cancelReservation(reservationId)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("BookingRepositoryImpl", "cancelReservation error", e)
            false
        }
    }

}