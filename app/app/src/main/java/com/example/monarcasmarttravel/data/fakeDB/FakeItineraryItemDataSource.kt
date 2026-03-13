package com.example.monarcasmarttravel.data.fakeDB

import android.util.Log
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.ui.screens.trip.PlanType
import com.example.monarcasmarttravel.utils.AppError
import java.util.Calendar

object FakeItineraryItemDataSource {

    private val calendar = Calendar.getInstance()

    private val items = mutableListOf(

        // ─── PARÍS (tripId = 2) ───────────────────────────────────────────────

        ItineraryItem(
            id = 1, tripId = 2, type = PlanType.FLIGHT,
            company = "Air France", transportNumber = "AF1024",
            origin = "Barcelona", destination = "Aeroport Charles de Gaulle",
            departureDate = calendar.apply { set(2026, Calendar.MAY, 15, 8, 30) }.time,
            price = 180.0
        ),
        ItineraryItem(
            id = 2, tripId = 2, type = PlanType.HOTEL,
            locationName = "Pullman Paris Tour Eiffel",
            address = "18 Avenue de Suffren",
            checkInDate = calendar.apply { set(2026, Calendar.MAY, 15, 8, 30) }.time,
            checkOutDate = calendar.apply { set(2026, Calendar.MAY, 22, 18, 0) }.time,
            price = 1400.0
        ),
        ItineraryItem(
            id = 3, tripId = 2, type = PlanType.LOCATION,
            locationName = "Museu del Louvre",
            address = "Rue de Rivoli, Paris",
            checkInDate = calendar.apply { set(2026, Calendar.MAY, 16, 10, 0) }.time,
            price = 17.0
        ),
        ItineraryItem(
            id = 4, tripId = 2, type = PlanType.RESTAURANT,
            locationName = "Le Jules Verne",
            address = "Torre Eiffel, 2on Pis",
            checkInDate = calendar.apply { set(2026, Calendar.MAY, 16, 20, 0) }.time,
            price = 250.0
        ),

        // ─── NOVA YORK (tripId = 3) ───────────────────────────────────────────

        ItineraryItem(
            id = 5, tripId = 3, type = PlanType.FLIGHT,
            company = "Delta Airlines", transportNumber = "DL201",
            origin = "Barcelona", destination = "Aeroport JFK",
            departureDate = calendar.apply { set(2026, Calendar.AUGUST, 10, 12, 0) }.time,
            price = 650.0
        ),
        ItineraryItem(
            id = 6, tripId = 3, type = PlanType.HOTEL,
            locationName = "Marriott Marquis",
            address = "Times Square, NY",
            checkInDate = calendar.apply { set(2026, Calendar.AUGUST, 10, 12, 0) }.time,
            checkOutDate = calendar.apply { set(2026, Calendar.AUGUST, 25, 11, 0) }.time,
            price = 2800.0
        ),
        ItineraryItem(
            id = 7, tripId = 3, type = PlanType.LOCATION,
            locationName = "Estàtua de la Llibertat",
            address = "Liberty Island",
            checkInDate = calendar.apply { set(2026, Calendar.AUGUST, 11, 9, 30) }.time,
            price = 25.0
        ),
        ItineraryItem(
            id = 8, tripId = 3, type = PlanType.RESTAURANT,
            locationName = "Joe's Pizza",
            address = "Greenwich Village",
            checkInDate = calendar.apply { set(2026, Calendar.AUGUST, 11, 13, 0) }.time,
            price = 15.0
        ),

        // ─── KYOTO (tripId = 1 / else) ────────────────────────────────────────

        ItineraryItem(
            id = 9, tripId = 1, type = PlanType.FLIGHT,
            company = "Japan Airlines", transportNumber = "JL123",
            origin = "Barcelona", destination = "Aeroport de Narita",
            departureDate = calendar.apply { set(2026, Calendar.MARCH, 23, 10, 30) }.time,
            price = 850.0
        ),
        ItineraryItem(
            id = 10, tripId = 1, type = PlanType.HOTEL,
            locationName = "Shinjuku Granbell Hotel",
            address = "2-14-5 Kabukicho, Shinjuku-ku",
            checkInDate = calendar.apply { set(2026, Calendar.MARCH, 23, 15, 0) }.time,
            checkOutDate = calendar.apply { set(2026, Calendar.MARCH, 30, 11, 0) }.time,
            price = 1200.0
        ),
        ItineraryItem(
            id = 11, tripId = 1, type = PlanType.LOCATION,
            locationName = "Temple Senso-ji",
            address = "2-3-1 Asakusa, Taito, Tòquio",
            checkInDate = calendar.apply { set(2026, Calendar.MARCH, 24, 10, 0) }.time,
            price = 0.0
        ),
        ItineraryItem(
            id = 12, tripId = 1, type = PlanType.RESTAURANT,
            locationName = "Ichiran Ramen Shinjuku",
            address = "3-34-11 Shinjuku, Tòquio",
            checkInDate = calendar.apply { set(2026, Calendar.MARCH, 24, 19, 0) }.time,
            price = 15.0
        ),
        ItineraryItem(
            id = 13, tripId = 1, type = PlanType.TRAIN,
            company = "JR Central", transportNumber = "Nozomi 215",
            origin = "Tòquio", destination = "Kyoto",
            departureDate = calendar.apply { set(2026, Calendar.MARCH, 25, 9, 30) }.time,
            price = 90.0
        ),
        ItineraryItem(
            id = 14, tripId = 1, type = PlanType.RESTAURANT,
            locationName = "Gion Karyo",
            address = "570-235 Gionmachi, Kyoto",
            checkInDate = calendar.apply { set(2026, Calendar.MARCH, 25, 20, 30) }.time,
            price = 120.5
        ),
        ItineraryItem(
            id = 15, tripId = 1, type = PlanType.BOAT,
            company = "JR West Ferry", transportNumber = "Miyajima Line",
            origin = "Hiroshima", destination = "Miyajima",
            departureDate = calendar.apply { set(2026, Calendar.MARCH, 26, 10, 0) }.time,
            price = 5.0
        ),
        ItineraryItem(
            id = 16, tripId = 1, type = PlanType.LOCATION,
            locationName = "Shibuya Sky Mirador",
            address = "2-24-12 Shibuya, Tòquio",
            checkInDate = calendar.apply { set(2026, Calendar.MARCH, 26, 12, 0) }.time,
            price = 25.0
        ),
        ItineraryItem(
            id = 17, tripId = 1, type = PlanType.TRAIN,
            company = "Odakyu Railways", transportNumber = "EXEα 30000",
            origin = "Shinjuku", destination = "Hakone",
            departureDate = calendar.apply { set(2026, Calendar.MARCH, 27, 8, 45) }.time,
            price = 22.0
        ),
        ItineraryItem(
            id = 18, tripId = 1, type = PlanType.LOCATION,
            locationName = "Fushimi Inari Taisha",
            address = "68 Fukakusa Yabunouchicho, Kyoto",
            checkInDate = calendar.apply { set(2026, Calendar.MARCH, 27, 11, 0) }.time,
            price = 0.0
        ),
        ItineraryItem(
            id = 19, tripId = 1, type = PlanType.LOCATION,
            locationName = "Parc dels Cérvols de Nara",
            address = "Nara, Japó",
            checkInDate = calendar.apply { set(2026, Calendar.MARCH, 29, 13, 0) }.time,
            price = 10.0
        ),
    )

    private var nextId = (items.maxOfOrNull { it.id } ?: 0) + 1

    fun getItemsByTrip(tripId: Int): List<ItineraryItem> =
        items.filter { it.tripId == tripId }

    fun getItemById(id: Int): ItineraryItem? =
        items.find { it.id == id }

    fun addItem(item: ItineraryItem): Int {
        val newItem = item.copy(id = nextId)
        nextId++

        items.add(newItem)
        Log.d("ItineraryItemDataSource", "Added item with ID: ${newItem.id} ${items.last()}")

        return AppError.OK.code
    }


    fun updateItem(item: ItineraryItem): Int {
        val index = items.indexOfFirst { it.id == item.id }
        if (index == -1) return AppError.NON_EXISTING_ITEM.code

        items[index] = item
        Log.d("ItineraryItemDataSource", "Updated item with ID: ${item.id} ${items[index]}")
        return AppError.OK.code
    }

    fun deleteItem(id: Int): Int {
        val debugSize = items.size
        val status = if (items.removeIf { it.id == id }) AppError.OK.code else AppError.NON_EXISTING_ITEM.code
        Log.d("ItineraryItemDataSource", "Deleted item with id: $id with status: $status ($debugSize -> ${items.size})")
        return status
    }
}