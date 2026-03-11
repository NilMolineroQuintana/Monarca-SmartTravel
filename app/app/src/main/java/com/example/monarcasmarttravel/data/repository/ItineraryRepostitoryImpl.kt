package com.example.monarcasmarttravel.data.repository

import android.util.Log
import com.example.monarcasmarttravel.data.fakeDB.FakeItineraryItemDataSource
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.domain.interfaces.ItineraryItemRepository
import javax.inject.Inject

class ItineraryItemRepositoryImpl @Inject constructor() : ItineraryItemRepository {

    private val dataSource = FakeItineraryItemDataSource

    override suspend fun getItemsByTrip(tripId: Int): List<ItineraryItem> =
        dataSource.getItemsByTrip(tripId)

    override suspend fun addItineraryItem(item: ItineraryItem): Boolean {
        val status = dataSource.addItem(item)
        Log.d("ItineraryItemRepositoryImpl", "Added item: $item with status: $status")
        return status
    }

    override suspend fun updateItineraryItem(item: ItineraryItem): Boolean =
        dataSource.updateItem(item)

    override suspend fun deleteItineraryItem(id: Int): Boolean =
        dataSource.deleteItem(id)
}