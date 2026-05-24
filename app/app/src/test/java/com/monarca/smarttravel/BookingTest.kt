package com.monarca.smarttravel

import com.monarca.smarttravel.data.remote.HotelAPIService
import com.monarca.smarttravel.data.repository.BookingRepositoryImpl
import com.monarca.smarttravel.domain.model.BookingData
import com.monarca.smarttravel.domain.model.BookingResponse
import com.monarca.smarttravel.domain.model.Hotel
import com.monarca.smarttravel.domain.model.HotelResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
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

    // ─── BOOK ROOM ──────────────────────────────────────────────────────────

    @Test
    fun `bookRoom success returns BookingResponse`() = runBlocking {
        val request = BookingData(
            hotel_id = "H1", room_id = "R1",
            start_date = "2026-06-01", end_date = "2026-06-05",
            guest_name = "Test User", guest_email = "test@test.com"
        )
        val mockResponse = BookingResponse(
            message = "Reserva confirmada", nights = 4,
            reservation = request.copy(id = "RES-001")
        )

        `when`(mockApiService.bookRoom("G12", request))
            .thenReturn(Response.success(mockResponse))

        val result = repository.bookRoom(request)

        assertNotNull("La respuesta no debería ser nula", result)
        assertEquals("RES-001", result?.reservation?.id)
        assertEquals(4, result?.nights)
    }

    @Test
    fun `bookRoom failure returns null`() = runBlocking {
        val request = BookingData(
            hotel_id = "H1", room_id = "R1",
            start_date = "2026-06-01", end_date = "2026-06-05",
            guest_name = "Test User", guest_email = "test@test.com"
        )
        val errorResponse = okhttp3.ResponseBody.create(null, "Error")
        `when`(mockApiService.bookRoom("G12", request))
            .thenReturn(Response.error(400, errorResponse))

        val result = repository.bookRoom(request)

        assertNull("En caso de error HTTP, debe devolver null", result)
    }

    // ─── CANCEL RESERVATION ─────────────────────────────────────────────────

    @Test
    fun `cancelReservation success returns true`() = runBlocking {
        `when`(mockApiService.cancelReservation("RES-001"))
            .thenReturn(Response.success(Unit))

        val result = repository.cancelReservation("RES-001")

        assertTrue("Cancelación exitosa debe retornar true", result)
    }

    @Test
    fun `cancelReservation failure returns false`() = runBlocking {
        val errorResponse = okhttp3.ResponseBody.create(null, "Not Found")
        `when`(mockApiService.cancelReservation("RES-999"))
            .thenReturn(Response.error(404, errorResponse))

        val result = repository.cancelReservation("RES-999")

        assertFalse("Error HTTP debe retornar false", result)
    }

    @Test
    fun `cancelReservation exception returns false`() = runBlocking {
        `when`(mockApiService.cancelReservation("RES-ERR"))
            .thenThrow(RuntimeException("Network error"))

        val result = repository.cancelReservation("RES-ERR")

        assertFalse("Excepción debe retornar false", result)
    }
}