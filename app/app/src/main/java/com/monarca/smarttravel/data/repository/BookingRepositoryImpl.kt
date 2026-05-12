package com.monarca.smarttravel.data.repository

import android.util.Log
import com.monarca.smarttravel.data.remote.HotelAPIService
import com.monarca.smarttravel.domain.interfaces.BookingRepository
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

}