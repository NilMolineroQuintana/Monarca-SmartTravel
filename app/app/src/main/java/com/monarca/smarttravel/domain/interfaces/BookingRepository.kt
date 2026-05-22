package com.monarca.smarttravel.domain.interfaces

import com.monarca.smarttravel.domain.model.BookingData
import com.monarca.smarttravel.domain.model.BookingResponse
import com.monarca.smarttravel.domain.model.Hotel

interface BookingRepository {

    suspend fun getAvailable(start_date: String, end_date: String, city: String): List<Hotel>?
    suspend fun bookRoom(bookingRequest: BookingData): BookingResponse?
    suspend fun cancelReservation(reservationId: String): Boolean
}