package com.example.monarcasmarttravel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.monarcasmarttravel.data.fakeDB.FakeTripDataSource
import com.example.monarcasmarttravel.data.repository.TripRepositoryImpl
import com.example.monarcasmarttravel.ui.viewmodels.TripViewModel
import com.example.monarcasmarttravel.utils.AppError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class TripViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TripViewModel

    @Before
    fun setUp() {
        FakeTripDataSource.reset()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = TripViewModel(
            repository = TripRepositoryImpl()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val validDateIn  = Calendar.getInstance().apply { set(2026, Calendar.SEPTEMBER, 1,  9,  0) }.time
    private val validDateOut = Calendar.getInstance().apply { set(2026, Calendar.SEPTEMBER, 10, 18, 0) }.time

    // ─── ADD TRIP ────────────────────────────────────────────────────────────

    @Test
    fun `addTrip with valid data should succeed`() {
        val sizeBefore = viewModel.trips.size

        val result = viewModel.addTrip(
            title = "Viena, Àustria",
            description = "Viatge clàssic a Viena",
            dateIn = validDateIn,
            dateOut = validDateOut,
            userId = 1
        )

        assertTrue(result)
        assertEquals(sizeBefore + 1, viewModel.trips.size)
        assertTrue(viewModel.trips.any { it.title == "Viena, Àustria" })
    }

    @Test
    fun `addTrip with blank title should return INVALID_TITLE`() {
        val result = viewModel.addTrip(
            title = "   ",
            description = "Descripció",
            dateIn = validDateIn,
            dateOut = validDateOut,
            userId = 1
        )

        assertFalse(result)
        assertEquals(AppError.INVALID_TITLE.name, viewModel.errorMessage)
    }

    @Test
    fun `addTrip with blank description should return INVALID_DESCRIPTION`() {
        val result = viewModel.addTrip(
            title = "Budapest",
            description = "",
            dateIn = validDateIn,
            dateOut = validDateOut,
            userId = 1
        )

        assertFalse(result)
        assertEquals(AppError.INVALID_DESCRIPTION.name, viewModel.errorMessage)
    }

    @Test
    fun `addTrip with endDate before startDate should return INVALID_DATE_RANGE`() {
        val result = viewModel.addTrip(
            title = "Amsterdam",
            description = "Viatge vàlid",
            dateIn = validDateOut,
            dateOut = validDateIn,
            userId = 1
        )

        assertFalse(result)
        assertEquals(AppError.INVALID_DATE_RANGE.name, viewModel.errorMessage)
    }

    @Test
    fun `addTrip with endDate equal to startDate should return INVALID_DATE_RANGE`() {
        val result = viewModel.addTrip(
            title = "Praga",
            description = "Viatge d'un dia",
            dateIn = validDateIn,
            dateOut = validDateIn,
            userId = 1
        )

        assertFalse(result)
        assertEquals(AppError.INVALID_DATE_RANGE.name, viewModel.errorMessage)
    }

    // ─── UPDATE TRIP ─────────────────────────────────────────────────────────

    @Test
    fun `updateTrip with valid data should succeed`() {
        val added = viewModel.addTrip("Viatge per actualitzar", "Desc", validDateIn, validDateOut, 1)
        assertTrue(added)
        val trip = viewModel.trips.find { it.title == "Viatge per actualitzar" }!!

        val newDateIn  = Calendar.getInstance().apply { set(2026, Calendar.SEPTEMBER, 1, 9, 0) }.time
        val newDateOut = Calendar.getInstance().apply { set(2026, Calendar.SEPTEMBER, 15, 18, 0) }.time

        val result = viewModel.updateTrip(
            tripId = trip.id,
            title = "Viatge actualitzat",
            description = "Nova descripció",
            dateIn = newDateIn,
            dateOut = newDateOut
        )

        assertTrue(result)
        assertEquals("Viatge actualitzat", viewModel.getTripById(trip.id)?.title)
        assertEquals("Nova descripció", viewModel.getTripById(trip.id)?.description)
    }

    @Test
    fun `updateTrip with blank title should return INVALID_TITLE`() {
        val result = viewModel.updateTrip(
            tripId = 1,
            title = "",
            description = "Descripció",
            dateIn = validDateIn,
            dateOut = validDateOut
        )

        assertFalse(result)
        assertEquals(AppError.INVALID_TITLE.name, viewModel.errorMessage)
    }

    @Test
    fun `updateTrip with blank description should return INVALID_DESCRIPTION`() {
        val result = viewModel.updateTrip(
            tripId = 1,
            title = "Títol vàlid",
            description = "",
            dateIn = validDateIn,
            dateOut = validDateOut
        )

        assertFalse(result)
        assertEquals(AppError.INVALID_DESCRIPTION.name, viewModel.errorMessage)
    }

    @Test
    fun `updateTrip with invalid date range should return INVALID_DATE_RANGE`() {
        val result = viewModel.updateTrip(
            tripId = 1,
            title = "Títol",
            description = "Descripció",
            dateIn = validDateOut,
            dateOut = validDateIn
        )

        assertFalse(result)
        assertEquals(AppError.INVALID_DATE_RANGE.name, viewModel.errorMessage)
    }

    @Test
    fun `updateTrip with non-existing id should return false`() {
        val result = viewModel.updateTrip(
            tripId = 9999,
            title = "Títol",
            description = "Descripció",
            dateIn = validDateIn,
            dateOut = validDateOut
        )

        assertFalse(result)
        assertEquals(AppError.NON_EXISTING_ITEM.name, viewModel.errorMessage)
    }

    // ─── DELETE TRIP ─────────────────────────────────────────────────────────

    @Test
    fun `deleteTrip should remove only that trip`() {
        viewModel.addTrip("Viatge temporal", "Per eliminar", validDateIn, validDateOut, 1)
        val tripToDelete = viewModel.trips.find { it.title == "Viatge temporal" }!!
        val sizeBefore = viewModel.trips.size

        val result = viewModel.deleteTrip(tripToDelete.id)

        assertTrue(result)
        assertEquals(sizeBefore - 1, viewModel.trips.size)
        assertNull(viewModel.getTripById(tripToDelete.id))
    }

    @Test
    fun `deleteTrip with non-existing id should return false`() {
        val result = viewModel.deleteTrip(9999)

        assertFalse(result)
        assertEquals(AppError.NON_EXISTING_ITEM.name, viewModel.errorMessage)
    }
}