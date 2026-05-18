package com.monarca.smarttravel

import com.monarca.smarttravel.data.remote.HotelAPIService
import com.monarca.smarttravel.data.repository.BookingRepositoryImpl
import com.monarca.smarttravel.domain.model.Hotel
import com.monarca.smarttravel.domain.model.HotelResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

class BookingTest {

    private lateinit var mockApiService: HotelAPIService
    private lateinit var repository: BookingRepositoryImpl

    @Before
    fun setup() {
        mockApiService = mock(HotelAPIService::class.java)
        repository = BookingRepositoryImpl(mockApiService)
    }

    @Test
    fun `getAvailable success returns list of hotels`() = runBlocking {
        val mockHotel = Hotel(
            id = "H1", name = "Hotel Monarca BCN", address = "Carrer Fals 123",
            rating = 5, image_url = "/img.jpg", rooms = emptyList()
        )
        val mockResponse = HotelResponse(available_hotels = listOf(mockHotel))

        `when`(mockApiService.checkAvailability("G12", "2026-05-10", "2026-05-15", "BCN"))
            .thenReturn(Response.success(mockResponse))

        val result = repository.getAvailable("2026-05-10", "2026-05-15", "BCN")

        assertNotNull("El resultado no debería ser nulo", result)
        assertEquals("Debería retornar 1 hotel", 1, result?.size)
        assertEquals("Hotel Monarca BCN", result?.first()?.name)
    }

    @Test
    fun `getAvailable failure returns null`() = runBlocking {
        val errorResponse = okhttp3.ResponseBody.create(null, "Not Found")
        `when`(mockApiService.checkAvailability("G12", "2026-05-10", "2026-05-15", "BCN"))
            .thenReturn(Response.error(404, errorResponse))

        val result = repository.getAvailable("2026-05-10", "2026-05-15", "BCN")

        assertNull("En caso de error HTTP, debe devolver null", result)
    }
}