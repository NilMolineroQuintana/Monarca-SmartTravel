package com.example.monarcasmarttravel.data.repository

import android.util.Log
import com.example.monarcasmarttravel.data.fakeDB.FakeItineraryItemDataSource
import com.example.monarcasmarttravel.domain.interfaces.ItineraryRepository
import com.example.monarcasmarttravel.domain.interfaces.TripRepository
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.utils.AppError
import java.util.Date
import javax.inject.Inject

class ItineraryRepositoryImpl @Inject constructor() : ItineraryRepository {

    private val TAG = "ItineraryRepositoryImpl"
    private val dataSource = FakeItineraryItemDataSource

    override suspend fun getItemsByTrip(tripId: Int): List<ItineraryItem> =
        dataSource.getItemsByTrip(tripId)

    override fun getItemById(id: Int): ItineraryItem? =
        dataSource.getItemById(id)

    override fun addItineraryItem(item: ItineraryItem): Int {
        val status = dataSource.addItem(item)
        Log.d(TAG, "Added item: $item with status: $status")
        return status
    }

    override fun updateItineraryItem(item: ItineraryItem): Int {
        val status = dataSource.updateItem(item)
        Log.d(TAG, "Updated item: $item with status: $status")
        return status
    }

    override fun deleteItineraryItem(id: Int): Int {
        val status = dataSource.deleteItem(id)
        Log.d(TAG, "Deleted item with id: $id with status: $status")
        return status
    }
}