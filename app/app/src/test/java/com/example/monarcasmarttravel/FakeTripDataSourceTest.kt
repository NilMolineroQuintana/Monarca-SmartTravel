package com.example.monarcasmarttravel

import com.example.monarcasmarttravel.data.fakeDB.FakeTripDataSource
import com.example.monarcasmarttravel.domain.model.Trip
import com.example.monarcasmarttravel.utils.AppError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class FakeTripDataSourceTest {

    @Before
    fun setUp() {
        FakeTripDataSource.reset()
    }

    // ─── ADD ────────────────────────────────────────────────────────────────

    @Test
    fun `addTrip should add trip to list`() {
        val newTrip = Trip(
            id = 999,
            title = "Lisboa, Portugal",
            description = "Viatge a Lisboa",
            dateIn = Calendar.getInstance().apply { set(2026, Calendar.JUNE, 1, 10, 0) }.time,
            dateOut = Calendar.getInstance().apply { set(2026, Calendar.JUNE, 10, 18, 0) }.time,
            imageResId = null,
            userId = 1
        )

        val created = FakeTripDataSource.addTrip(newTrip)

        val allTrips = FakeTripDataSource.getAllTrips()
        assertEquals(4, allTrips.size)
        assertEquals("Lisboa, Portugal", created.title)
        assertEquals(1, allTrips.count { it.title == "Lisboa, Portugal" })
    }

    // ─── GET ────────────────────────────────────────────────────────────────

    @Test
    fun `getAllTrips should return all trips`() {
        val trips = FakeTripDataSource.getAllTrips()

        assertTrue(trips.isNotEmpty())
        assertTrue(trips.all { it.id > 0 })
    }

    @Test
    fun `getTripById should return correct trip`() {
        val trip = FakeTripDataSource.getTripById(1)

        assertNotNull(trip)
        assertEquals(1, trip!!.id)
        assertEquals("Kyoto, Japó", trip.title)
    }

    @Test
    fun `getTripById with invalid id should return null`() {
        val trip = FakeTripDataSource.getTripById(9999)

        assertNull(trip)
    }

    // ─── UPDATE ─────────────────────────────────────────────────────────────

    @Test
    fun `updateTrip should modify existing trip`() {
        val original = FakeTripDataSource.getTripById(1)
        assertNotNull(original)

        val updated = original!!.copy(title = "Osaka, Japó", description = "Nova descripció")
        val status = FakeTripDataSource.updateTrip(updated)

        assertEquals(AppError.OK.code, status)
        val retrieved = FakeTripDataSource.getTripById(1)
        assertEquals("Osaka, Japó", retrieved?.title)
        assertEquals("Nova descripció", retrieved?.description)
    }

    @Test
    fun `updateTrip with non-existing id should return NON_EXISTING_ITEM`() {
        val fakeTrip = Trip(
            id = 9999,
            title = "Viatge inexistent",
            description = "No existeix",
            dateIn = Calendar.getInstance().time,
            dateOut = Calendar.getInstance().time,
            userId = 1
        )

        val status = FakeTripDataSource.updateTrip(fakeTrip)

        assertEquals(AppError.NON_EXISTING_ITEM.code, status)
    }

    // ─── DELETE ─────────────────────────────────────────────────────────────

    @Test
    fun `deleteTrip should remove only that trip`() {
        val newTrip = Trip(
            id = 0,
            title = "Viatge a eliminar",
            description = "Per eliminar",
            dateIn = Calendar.getInstance().apply { set(2026, Calendar.JUNE, 1, 10, 0) }.time,
            dateOut = Calendar.getInstance().apply { set(2026, Calendar.JUNE, 10, 18, 0) }.time,
            imageResId = null,
            userId = 1
        )
        val created = FakeTripDataSource.addTrip(newTrip)
        val addedId = created.id

        val status = FakeTripDataSource.deleteTrip(addedId)

        assertEquals(AppError.OK.code, status)
        assertNull(FakeTripDataSource.getTripById(addedId))
        assertEquals(3, FakeTripDataSource.getAllTrips().size)
    }

    @Test
    fun `deleteTrip with non-existing id should return NON_EXISTING_ITEM`() {
        val status = FakeTripDataSource.deleteTrip(9999)

        assertEquals(AppError.NON_EXISTING_ITEM.code, status)
    }
}