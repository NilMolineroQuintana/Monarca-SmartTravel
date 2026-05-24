package com.monarca.smarttravel.viewmodels

import android.content.Context
import com.monarca.smarttravel.domain.interfaces.AuthRepository
import com.monarca.smarttravel.domain.interfaces.BookingRepository
import com.monarca.smarttravel.domain.interfaces.ItineraryRepository
import com.monarca.smarttravel.domain.interfaces.TripRepository
import com.monarca.smarttravel.domain.model.ItineraryItem
import com.monarca.smarttravel.domain.model.Trip
import com.monarca.smarttravel.ui.screens.trip.PlanType
import com.monarca.smarttravel.ui.viewmodels.TripViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class TripViewModelTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    private lateinit var context: Context
    private lateinit var repository: TripRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var bookingRepository: BookingRepository
    private lateinit var itineraryRepository: ItineraryRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        context = mock(Context::class.java)
        `when`(context.packageName).thenReturn("com.monarca.smarttravel")
        repository = mock(TripRepository::class.java)
        authRepository = mock(AuthRepository::class.java)
        bookingRepository = mock(BookingRepository::class.java)
        itineraryRepository = mock(ItineraryRepository::class.java)
        runBlocking {
            `when`(authRepository.getLoggedUID()).thenReturn("test-user")
        }
        `when`(repository.getTripsByUser("test-user")).thenReturn(flowOf(emptyList()))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ─── TRIPS SORTING ──────────────────────────────────────────────────────

    @Test
    fun `trips sorted by date proximity ascending`() = runTest(testDispatcher + testScheduler) {
        val now = System.currentTimeMillis()
        val farTrip = Trip(id = 2, title = "Far", description = "",
            dateIn = Date(now + 100_000), dateOut = Date(now + 200_000), userId = "test-user")
        val nearTrip = Trip(id = 1, title = "Near", description = "",
            dateIn = Date(now + 10_000), dateOut = Date(now + 50_000), userId = "test-user")

        `when`(repository.getTripsByUser("test-user")).thenReturn(flowOf(listOf(farTrip, nearTrip)))

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        val trips = viewModel.trips.first { it.isNotEmpty() }
        assertEquals(2, trips.size)
        assertEquals("Near", trips[0].title)
        assertEquals("Far", trips[1].title)
    }

    @Test
    fun `trips with same proximity sorted by id descending`() = runTest(testDispatcher + testScheduler) {
        val now = System.currentTimeMillis()
        val tripA = Trip(id = 10, title = "Older", description = "",
            dateIn = Date(now + 50_000), dateOut = Date(now + 100_000), userId = "test-user")
        val tripB = Trip(id = 20, title = "Newer", description = "",
            dateIn = Date(now + 50_000), dateOut = Date(now + 100_000), userId = "test-user")

        `when`(repository.getTripsByUser("test-user")).thenReturn(flowOf(listOf(tripA, tripB)))

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        val trips = viewModel.trips.first { it.isNotEmpty() }
        assertEquals("Newer", trips[0].title)
        assertEquals("Older", trips[1].title)
    }

    // ─── RESERVATIONS FILTERING ─────────────────────────────────────────────

    @Test
    fun `reservations returns only trips with non-null reservationId`() = runTest(testDispatcher + testScheduler) {
        val now = System.currentTimeMillis()
        val tripWithReservation = Trip(id = 1, title = "With Reservation", description = "",
            dateIn = Date(now), dateOut = Date(now + 100_000), userId = "test-user", reservationId = "RES-001")
        val tripWithoutReservation = Trip(id = 2, title = "Without Reservation", description = "",
            dateIn = Date(now + 200_000), dateOut = Date(now + 300_000), userId = "test-user")

        `when`(repository.getTripsByUser("test-user")).thenReturn(flowOf(listOf(tripWithoutReservation, tripWithReservation)))

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        val reservations = viewModel.reservations.first { it.isNotEmpty() }
        assertEquals(1, reservations.size)
        assertEquals("With Reservation", reservations[0].title)
        assertEquals("RES-001", reservations[0].reservationId)
    }

    @Test
    fun `reservations is empty when no trips have reservationId`() = runTest(testDispatcher + testScheduler) {
        val now = System.currentTimeMillis()
        val trip = Trip(id = 1, title = "Simple Trip", description = "",
            dateIn = Date(now), dateOut = Date(now + 100_000), userId = "test-user")

        `when`(repository.getTripsByUser("test-user")).thenReturn(flowOf(listOf(trip)))

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        val reservations = viewModel.reservations.first { it.isEmpty() }
        assertTrue(reservations.isEmpty())
    }

    // ─── addTripFromBooking ─────────────────────────────────────────────────

    @Test
    fun `addTripFromBooking stores all reservation fields`() = runTest(testDispatcher + testScheduler) {
        val dateIn = Date()
        val dateOut = Date()
        val expectedTrip = Trip(
            id = 0, title = "Viaje a Barcelona", description = "Reserva: double",
            dateIn = dateIn, dateOut = dateOut, userId = "test-user",
            reservationId = "RES-001", hotelName = "Hotel Monarca BCN",
            hotelImageUrl = "http://example.com/hotel.jpg", roomType = "double",
            imageURL = "android.resource://com.monarca.smarttravel/drawable/barcelona"
        )
        val capturedTrip = arrayOfNulls<Trip>(1)
        `when`(repository.addTrip(expectedTrip)).thenAnswer { inv ->
            capturedTrip[0] = inv.getArgument(0)
            42L
        }

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        viewModel.addTripFromBooking(
            hotelName = "Hotel Monarca BCN",
            hotelAddress = "Carrer Fals 123",
            roomType = "double",
            dateIn = dateIn,
            dateOut = dateOut,
            reservationId = "RES-001",
            hotelImageUrl = "http://example.com/hotel.jpg",
            totalPrice = 200.0,
            cityName = "Barcelona"
        )
        advanceUntilIdle()

        val trip = capturedTrip[0]!!
        assertEquals("RES-001", trip.reservationId)
        assertEquals("Hotel Monarca BCN", trip.hotelName)
        assertEquals("http://example.com/hotel.jpg", trip.hotelImageUrl)
        assertEquals("double", trip.roomType)
        assertEquals("Viaje a Barcelona", trip.title)
        assertEquals("Reserva: double", trip.description)
    }

    @Test
    fun `addTripFromBooking uses city drawable URI for Barcelona`() = runTest(testDispatcher + testScheduler) {
        val dateIn = Date()
        val dateOut = Date()
        val expectedTrip = Trip(
            id = 0, title = "Viaje a Barcelona", description = "Reserva: single",
            dateIn = dateIn, dateOut = dateOut, userId = "test-user",
            reservationId = "RES-001", hotelName = "Hotel",
            hotelImageUrl = "http://hotel.jpg", roomType = "single",
            imageURL = "android.resource://com.monarca.smarttravel/drawable/barcelona"
        )
        val capturedTrip = arrayOfNulls<Trip>(1)
        `when`(repository.addTrip(expectedTrip)).thenAnswer { inv ->
            capturedTrip[0] = inv.getArgument(0)
            42L
        }

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        viewModel.addTripFromBooking(
            hotelName = "Hotel", hotelAddress = "", roomType = "single",
            dateIn = dateIn, dateOut = dateOut, reservationId = "RES-001",
            hotelImageUrl = "http://hotel.jpg", totalPrice = 100.0, cityName = "Barcelona"
        )
        advanceUntilIdle()

        assertEquals("android.resource://com.monarca.smarttravel/drawable/barcelona", capturedTrip[0]!!.imageURL)
    }

    @Test
    fun `addTripFromBooking uses Paris drawable when city is Paris`() = runTest(testDispatcher + testScheduler) {
        val dateIn = Date()
        val dateOut = Date()
        val expectedTrip = Trip(
            id = 0, title = "Viaje a Paris", description = "Reserva: single",
            dateIn = dateIn, dateOut = dateOut, userId = "test-user",
            reservationId = "RES-002", hotelName = "Hotel",
            hotelImageUrl = "http://hotel.jpg", roomType = "single",
            imageURL = "android.resource://com.monarca.smarttravel/drawable/paris"
        )
        val capturedTrip = arrayOfNulls<Trip>(1)
        `when`(repository.addTrip(expectedTrip)).thenAnswer { inv ->
            capturedTrip[0] = inv.getArgument(0)
            42L
        }

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        viewModel.addTripFromBooking(
            hotelName = "Hotel", hotelAddress = "", roomType = "single",
            dateIn = dateIn, dateOut = dateOut, reservationId = "RES-002",
            hotelImageUrl = "http://hotel.jpg", totalPrice = 100.0, cityName = "Paris"
        )
        advanceUntilIdle()

        assertEquals("android.resource://com.monarca.smarttravel/drawable/paris", capturedTrip[0]!!.imageURL)
    }

    @Test
    fun `addTripFromBooking uses London drawable when city is London`() = runTest(testDispatcher + testScheduler) {
        val dateIn = Date()
        val dateOut = Date()
        val expectedTrip = Trip(
            id = 0, title = "Viaje a London", description = "Reserva: single",
            dateIn = dateIn, dateOut = dateOut, userId = "test-user",
            reservationId = "RES-003", hotelName = "Hotel",
            hotelImageUrl = "http://hotel.jpg", roomType = "single",
            imageURL = "android.resource://com.monarca.smarttravel/drawable/london"
        )
        val capturedTrip = arrayOfNulls<Trip>(1)
        `when`(repository.addTrip(expectedTrip)).thenAnswer { inv ->
            capturedTrip[0] = inv.getArgument(0)
            42L
        }

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        viewModel.addTripFromBooking(
            hotelName = "Hotel", hotelAddress = "", roomType = "single",
            dateIn = dateIn, dateOut = dateOut, reservationId = "RES-003",
            hotelImageUrl = "http://hotel.jpg", totalPrice = 100.0, cityName = "London"
        )
        advanceUntilIdle()

        assertEquals("android.resource://com.monarca.smarttravel/drawable/london", capturedTrip[0]!!.imageURL)
    }

    @Test
    fun `addTripFromBooking creates HOTEL itinerary item`() = runTest(testDispatcher + testScheduler) {
        val dateIn = Date()
        val dateOut = Date()
        val expectedTrip = Trip(
            id = 0, title = "Viaje a Barcelona", description = "Reserva: suite",
            dateIn = dateIn, dateOut = dateOut, userId = "test-user",
            reservationId = "RES-001", hotelName = "Hotel BCN",
            hotelImageUrl = null, roomType = "suite",
            imageURL = "android.resource://com.monarca.smarttravel/drawable/barcelona"
        )
        val capturedTrip = arrayOfNulls<Trip>(1)
        `when`(repository.addTrip(expectedTrip)).thenAnswer { inv ->
            capturedTrip[0] = inv.getArgument(0)
            42L
        }

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        viewModel.addTripFromBooking(
            hotelName = "Hotel BCN", hotelAddress = "Carrer 123", roomType = "suite",
            dateIn = dateIn, dateOut = dateOut, reservationId = "RES-001",
            hotelImageUrl = null, totalPrice = 300.0, cityName = "Barcelona"
        )
        advanceUntilIdle()

        val expectedItem = ItineraryItem(
            id = 0, tripId = 42, type = PlanType.HOTEL,
            price = 300.0, locationName = "Hotel BCN",
            address = "Carrer 123", checkInDate = dateIn,
            isFromReservation = true
        )
        Mockito.verify(itineraryRepository).addItineraryItem(expectedItem)
    }

    // ─── deleteTrip ─────────────────────────────────────────────────────────

    @Test
    fun `deleteTrip with reservationId cancels via API then deletes locally`() = runTest(testDispatcher + testScheduler) {
        val existingTrip = Trip(id = 5, title = "Trip", description = "",
            dateIn = Date(), dateOut = Date(), userId = "test-user", reservationId = "RES-001")
        `when`(repository.getTripById(5)).thenReturn(existingTrip)
        `when`(bookingRepository.cancelReservation("RES-001")).thenReturn(true)

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        viewModel.deleteTrip(5)
        advanceUntilIdle()

        Mockito.verify(bookingRepository).cancelReservation("RES-001")
        Mockito.verify(repository).deleteTrip(5)
    }

    @Test
    fun `deleteTrip without reservationId skips API call`() = runTest(testDispatcher + testScheduler) {
        val existingTrip = Trip(id = 3, title = "Simple", description = "",
            dateIn = Date(), dateOut = Date(), userId = "test-user")
        `when`(repository.getTripById(3)).thenReturn(existingTrip)

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        viewModel.deleteTrip(3)
        advanceUntilIdle()

        Mockito.verify(bookingRepository, never()).cancelReservation("_")
        Mockito.verify(repository).deleteTrip(3)
    }

    // ─── nextTrip ───────────────────────────────────────────────────────────

    @Test
    fun `nextTrip returns closest future trip`() = runTest(testDispatcher + testScheduler) {
        val now = System.currentTimeMillis()
        val past = Trip(id = 1, title = "Past", description = "",
            dateIn = Date(now - 200_000), dateOut = Date(now - 100_000), userId = "test-user")
        val nearFuture = Trip(id = 2, title = "Soon", description = "",
            dateIn = Date(now + 10_000), dateOut = Date(now + 50_000), userId = "test-user")
        val farFuture = Trip(id = 3, title = "Later", description = "",
            dateIn = Date(now + 100_000), dateOut = Date(now + 200_000), userId = "test-user")

        `when`(repository.getTripsByUser("test-user")).thenReturn(flowOf(listOf(past, farFuture, nearFuture)))

        val viewModel = TripViewModel(context, repository, authRepository, bookingRepository, itineraryRepository)

        viewModel.trips.first { it.size == 3 }
        val next = viewModel.nextTrip.first { it != null }
        assertEquals("Soon", next!!.title)
    }
}
