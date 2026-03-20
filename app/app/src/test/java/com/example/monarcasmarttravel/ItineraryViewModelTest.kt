package com.example.monarcasmarttravel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.monarcasmarttravel.data.fakeDB.FakeItineraryItemDataSource
import com.example.monarcasmarttravel.data.repository.ItineraryRepositoryImpl
import com.example.monarcasmarttravel.data.repository.TripRepositoryImpl
import com.example.monarcasmarttravel.ui.screens.trip.PlanFormState
import com.example.monarcasmarttravel.ui.viewmodels.ItineraryViewModel
import com.example.monarcasmarttravel.utils.AppError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ItineraryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ItineraryViewModel

    @Before
    fun setUp() {
        FakeItineraryItemDataSource.reset()
        // needed FakeTripDataSource.reset()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = ItineraryViewModel(
            repository = ItineraryRepositoryImpl(),
            tripRepository = TripRepositoryImpl()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addItem within trip date range should succeed`() {
        val form = PlanFormState(
            locationName = "Fushimi Inari",
            destination = "", company = "", transportNumber = "",
            address = "Kyoto", checkInDate = "25/03/2026 10:00", price = 0.0
        )
        assertEquals(AppError.OK.code, viewModel.addItem(tripId = 1, ruta = "location", form = form))
    }

    @Test
    fun `addItem before trip startDate should return ITEM_OUT_OF_RANGE`() {
        val form = PlanFormState(
            locationName = "Lloc invàlid",
            destination = "", company = "", transportNumber = "",
            address = "Kyoto", checkInDate = "01/01/2026 10:00", price = 0.0
        )
        assertEquals(AppError.ITEM_OUT_OF_RANGE.code, viewModel.addItem(tripId = 1, ruta = "location", form = form))
    }

    @Test
    fun `addItem after trip endDate should return ITEM_OUT_OF_RANGE`() {
        val form = PlanFormState(
            locationName = "Lloc invàlid",
            destination = "", company = "", transportNumber = "",
            address = "Kyoto", checkInDate = "01/12/2026 10:00", price = 0.0
        )
        assertEquals(AppError.ITEM_OUT_OF_RANGE.code, viewModel.addItem(tripId = 1, ruta = "location", form = form))
    }

    @Test
    fun `addItem on exactly trip startDate should succeed`() {
        val form = PlanFormState(
            locationName = "Aeroport",
            destination = "", company = "", transportNumber = "",
            address = "Kyoto", checkInDate = "23/03/2026 10:30", price = 0.0
        )
        assertEquals(AppError.OK.code, viewModel.addItem(tripId = 1, ruta = "location", form = form))
    }

    @Test
    fun `addItem on exactly trip endDate should succeed`() {
        val form = PlanFormState(
            locationName = "Aeroport tornada",
            destination = "", company = "", transportNumber = "",
            address = "Kyoto", checkInDate = "30/03/2026 15:00", price = 0.0
        )
        assertEquals(AppError.OK.code, viewModel.addItem(tripId = 1, ruta = "location", form = form))
    }

    @Test
    fun `addItem with invalid route should return UNKNOWN`() {
        val form = PlanFormState(
            locationName = "Lloc",
            destination = "", company = "", transportNumber = "",
            address = "Kyoto", checkInDate = "data-incorrecta", price = 0.0
        )
        assertEquals(AppError.UNKNOWN.code, viewModel.addItem(tripId = 1, ruta = "incorrect", form = form))
    }

    @Test
    fun `addItem with invalid date format should return NON_EXISTING_DATE`() {
        val form = PlanFormState(
            locationName = "Lloc",
            destination = "", company = "", transportNumber = "",
            address = "Kyoto", checkInDate = "data-incorrecta", price = 0.0
        )
        assertEquals(AppError.NON_EXISTING_DATE.code, viewModel.addItem(tripId = 1, ruta = "location", form = form))
    }

    // ─── UPDATE ─────────────────────────────────────────────────────────────────

    @Test
    fun `updateItem with valid data should return OK`() {
        val form = makeForm(checkInDate = "18/05/2026 11:00", locationName = "Louvre (actualitzat)")

        val status = viewModel.updateItem(itemId = 3, ruta = "location", form = form)

        assertEquals(AppError.OK.code, status)
        val updated = viewModel.getItemById(3)
        assertEquals("Louvre (actualitzat)", updated?.locationName)
    }

    @Test
    fun `updateItem with non-existing id should return NON_EXISTING_ITEM`() {
        val form = makeForm(checkInDate = "18/05/2026 11:00")

        val status = viewModel.updateItem(itemId = 9999, ruta = "location", form = form)

        assertEquals(AppError.NON_EXISTING_ITEM.code, status)
    }

    @Test
    fun `updateItem with invalid date format should return NON_EXISTING_DATE`() {
        val form = makeForm(checkInDate = "data-incorrecta")

        val status = viewModel.updateItem(itemId = 3, ruta = "location", form = form)

        assertEquals(AppError.NON_EXISTING_DATE.code, status)
    }

    @Test
    fun `updateItem with date out of trip range should return ITEM_OUT_OF_RANGE`() {
        val form = makeForm(checkInDate = "01/01/2026 10:00")

        val status = viewModel.updateItem(itemId = 3, ruta = "location", form = form)

        assertEquals(AppError.ITEM_OUT_OF_RANGE.code, status)
    }

    private fun makeForm(checkInDate: String, locationName: String = "Test") = PlanFormState(
        locationName = locationName,
        destination = "",
        company = "",
        transportNumber = "",
        address = "Test address",
        checkInDate = checkInDate,
        price = 0.0
    )
}