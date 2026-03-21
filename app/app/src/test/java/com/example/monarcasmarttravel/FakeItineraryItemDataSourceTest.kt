package com.example.monarcasmarttravel

import com.example.monarcasmarttravel.data.fakeDB.FakeItineraryItemDataSource
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.ui.screens.trip.PlanType
import com.example.monarcasmarttravel.utils.AppError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class FakeItineraryItemDataSourceTest {

    // ─── ADD ────────────────────────────────────────────────────────────────

    @Test
    fun `addItem should add activity to trip`() {
        val newItem = ItineraryItem(
            id = 999,
            tripId = 99,
            type = PlanType.LOCATION,
            locationName = "Sagrada Família",
            address = "C/ de Mallorca, 401, Barcelona",
            checkInDate = Calendar.getInstance().apply { set(2026, Calendar.JUNE, 10, 10, 0) }.time,
            price = 26.0
        )

        // Act
        val status = FakeItineraryItemDataSource.addItem(newItem)

        // Assert
        assertEquals(AppError.OK.code, status)
        val itemsForTrip = FakeItineraryItemDataSource.getItemsByTrip(99)
        assertEquals(1, itemsForTrip.size)
        assertEquals("Sagrada Família", itemsForTrip.first().locationName)
        assertEquals(99, itemsForTrip.first().tripId)
    }

    // ─── GET ────────────────────────────────────────────────────────────────

    @Test
    fun `getActivitiesByTrip should return only that trip's activities`() {
        val itemsForTrip = FakeItineraryItemDataSource.getItemsByTrip(1)

        assertTrue(itemsForTrip.isNotEmpty())
        assertTrue(itemsForTrip.all { it.tripId == 1 })
    }

    @Test
    fun `getActivitiesByTrip with invalid tripId should return empty list`() {
        val itemsForTrip = FakeItineraryItemDataSource.getItemsByTrip(9999)

        assertTrue(itemsForTrip.isEmpty())
    }

    // ─── UPDATE ─────────────────────────────────────────────────────────────

    @Test
    fun `updateItem should modify existing activity`() {
        val original = FakeItineraryItemDataSource.getItemById(1)
        assertNotNull(original)

        val updated = original!!.copy(locationName = "Aeroport El Prat", price = 999.0)

        // Act
        val status = FakeItineraryItemDataSource.updateItem(updated)

        // Assert
        assertEquals(AppError.OK.code, status)
        val retrieved = FakeItineraryItemDataSource.getItemById(1)
        assertEquals("Aeroport El Prat", retrieved?.locationName)
        assertEquals(999.0, retrieved?.price)
    }

    @Test
    fun `updateItem with non-existing id should return NON_EXISTING_ITEM`() {
        // Arrange: item amb id que no existeix
        val fakeItem = ItineraryItem(
            id = 9999,
            tripId = 1,
            type = PlanType.LOCATION,
            locationName = "Lloc inexistent",
            price = 0.0
        )

        // Act
        val status = FakeItineraryItemDataSource.updateItem(fakeItem)

        // Assert
        assertEquals(AppError.NON_EXISTING_ITEM.code, status)
    }

    // ─── DELETE ─────────────────────────────────────────────────────────────

    @Test
    fun `deleteItem should remove only that activity`() {
        // Arrange: afegim un item nou per no tocar el dataset original
        val newItem = ItineraryItem(
            id = 0,
            tripId = 88,
            type = PlanType.RESTAURANT,
            locationName = "Restaurant de test",
            price = 15.0
        )
        FakeItineraryItemDataSource.addItem(newItem)
        val addedId = FakeItineraryItemDataSource.getItemsByTrip(88).first().id

        // Act
        val status = FakeItineraryItemDataSource.deleteItem(addedId)

        // Assert
        assertEquals(AppError.OK.code, status)
        assertTrue(FakeItineraryItemDataSource.getItemsByTrip(88).isEmpty())
    }

    @Test
    fun `deleteItem with non-existing id should return NON_EXISTING_ITEM`() {
        // Act
        val status = FakeItineraryItemDataSource.deleteItem(9999)

        // Assert
        assertEquals(AppError.NON_EXISTING_ITEM.code, status)
    }
}