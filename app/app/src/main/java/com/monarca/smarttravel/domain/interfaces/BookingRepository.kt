package com.monarca.smarttravel.domain.interfaces

interface BookingRepository {

    suspend fun getAvailable(start_date: String, end_date: String, city: String)
}