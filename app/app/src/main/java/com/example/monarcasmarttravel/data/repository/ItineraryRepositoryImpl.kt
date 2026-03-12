package com.example.monarcasmarttravel.data.repository

import android.util.Log
import com.example.monarcasmarttravel.data.fakeDB.FakeItineraryItemDataSource
import com.example.monarcasmarttravel.domain.interfaces.ItineraryRepository
import com.example.monarcasmarttravel.domain.interfaces.TripRepository
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import javax.inject.Inject

class ItineraryRepositoryImpl @Inject constructor(
    private val tripRepository: TripRepository
) : ItineraryRepository {

    private val dataSource = FakeItineraryItemDataSource

    override suspend fun getItemsByTrip(tripId: Int): List<ItineraryItem> =
        dataSource.getItemsByTrip(tripId)

    override fun addItineraryItem(item: ItineraryItem): Boolean {
        val status = dataSource.addItem(item)
        Log.d("ItineraryRepositoryImpl", "Added item: $item with status: $status")
        return status
    }

    override fun updateItineraryItem(item: ItineraryItem): Boolean =
        dataSource.updateItem(item)

    override fun deleteItineraryItem(id: Int): Boolean {
        val status = dataSource.deleteItem(id)
        Log.d("ItineraryRepositoryImpl", "Deleted item with id: $id with status: $status")
        return status
    }
}