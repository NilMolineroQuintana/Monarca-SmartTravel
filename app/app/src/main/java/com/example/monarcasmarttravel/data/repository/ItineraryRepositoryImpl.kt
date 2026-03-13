package com.example.monarcasmarttravel.data.repository

import android.util.Log
import com.example.monarcasmarttravel.data.fakeDB.FakeItineraryItemDataSource
import com.example.monarcasmarttravel.domain.interfaces.ItineraryRepository
import com.example.monarcasmarttravel.domain.interfaces.TripRepository
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.utils.AppError
import java.util.Date
import javax.inject.Inject

class ItineraryRepositoryImpl @Inject constructor(
    private val tripRepository: TripRepository
) : ItineraryRepository {

    private val dataSource = FakeItineraryItemDataSource

    override suspend fun getItemsByTrip(tripId: Int): List<ItineraryItem> =
        dataSource.getItemsByTrip(tripId)

    override fun getItemById(id: Int): ItineraryItem? =
        dataSource.getItemById(id)

    override fun addItineraryItem(item: ItineraryItem): Int {
        var status = validateItinerary(item)

        Log.d("ItineraryRepositoryImpl", "Validation status: $status")

        if (status != AppError.OK.code) {
            return status
        }

        status = dataSource.addItem(item)

        Log.d("ItineraryRepositoryImpl", "Added item: $item with status: $status")
        return status
    }

    override fun updateItineraryItem(item: ItineraryItem): Int {
        val status = validateItinerary(item)

        Log.d("ItineraryRepositoryImpl", "Validation status: $status")

        if (status != AppError.OK.code) {
            return status
        }

        Log.d("ItineraryRepositoryImpl", "Updating item: $item")

        return dataSource.updateItem(item)
    }

    override fun deleteItineraryItem(id: Int): Int {
        val status = dataSource.deleteItem(id)
        Log.d("ItineraryRepositoryImpl", "Deleted item with id: $id with status: $status")
        return status
    }

    private fun Date.toMinutes() = time / 60000

    private fun validateItinerary(item: ItineraryItem): Int {
        val itemDate = item.getInDate() ?: return AppError.NON_EXISTING_DATE.code
        val trip = tripRepository.getTripById(item.tripId) ?: return AppError.NON_EXISTING_TRIP.code

        if (itemDate.toMinutes() !in trip.dateIn.toMinutes()..trip.dateOut.toMinutes()) {
            return AppError.ITEM_OUT_OF_RANGE.code
        }

        return AppError.OK.code
    }
}