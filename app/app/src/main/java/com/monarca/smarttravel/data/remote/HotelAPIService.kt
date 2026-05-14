package com.monarca.smarttravel.data.remote

import com.monarca.smarttravel.domain.model.BookingData
import com.monarca.smarttravel.domain.model.BookingResponse
import com.monarca.smarttravel.domain.model.HotelResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HotelAPIService {

    @GET("hotels/{gid}/availability")
    suspend fun checkAvailability(
        @Path("gid") gid: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("city") city: String
    ): Response<HotelResponse>

    @POST("hotels/{gid}/reserve")
    suspend fun bookRoom(
        @Path("gid") gid: String,
        @Body bookingRequest: BookingData
    ): Response<BookingResponse>
}