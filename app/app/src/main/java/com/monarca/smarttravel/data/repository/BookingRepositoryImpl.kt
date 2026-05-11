package com.monarca.smarttravel.data.repository

import com.monarca.smarttravel.data.remote.HotelAPIService
import com.monarca.smarttravel.domain.interfaces.BookingRepository
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val hotelApiService: HotelAPIService
): BookingRepository {
    private val gid = "G12"

    override suspend fun getAvailable(start_date: String, end_date: String, city: String) {
        val response = hotelApiService.checkAvailability(gid, start_date, end_date, city)

        if (response.isSuccessful) {
            val hotelList = response.body()?.available_hotels

            hotelList?.forEach { hotel ->
                println("Hotel: ${hotel.name}")
                hotel.rooms.forEach { room ->
                    println("Room: ${room.room_type}, Price: ${room.price}")
                }
            }
        }
    }

}