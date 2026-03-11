package com.example.monarcasmarttravel.data.fakeDB

import com.example.monarcasmarttravel.domain.ItineraryItem
import com.example.monarcasmarttravel.ui.screens.PlanType
import java.util.Date

object FakeItineraryItemDataSource {

    private val items = mutableListOf(
        ItineraryItem(
            id = 1,
            tripId = 1,
            type = PlanType.FLIGHT,
            price = 150.0,
            origin = "Barcelona",
            destination = "París",
            company = "Vueling",
            transportNumber = "VY1234",
            departureDate = Date()
        ),
        ItineraryItem(
            id = 2,
            tripId = 1,
            type = PlanType.HOTEL,
            price = 200.0,
            locationName = "Hotel Le Marais",
            address = "Rue de Rivoli, París",
            checkInDate = Date(),
            checkOutDate = Date()
        ),
        ItineraryItem(
            id = 3,
            tripId = 1,
            type = PlanType.RESTAURANT,
            price = 45.0,
            locationName = "Le Petit Bistro",
            address = "Champs-Élysées, París",
            checkInDate = Date()
        )
    )

    fun getItemsByTrip(tripId: Int): List<ItineraryItem> =
        items.filter { it.tripId == tripId }

    fun addItem(item: ItineraryItem): Boolean {
        items.add(item)
        return true
    }

    fun updateItem(item: ItineraryItem): Boolean {
        val index = items.indexOfFirst { it.id == item.id }
        if (index == -1) return false
        items[index] = item
        return true
    }

    fun deleteItem(id: Int): Boolean {
        return items.removeIf { it.id == id }
    }
}