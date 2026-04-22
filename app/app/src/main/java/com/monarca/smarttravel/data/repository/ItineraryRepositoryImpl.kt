package com.monarca.smarttravel.data.repository

import android.util.Log
import com.monarca.smarttravel.data.fakeDB.FakeItineraryItemDataSource
import com.monarca.smarttravel.domain.interfaces.ItineraryRepository
import com.monarca.smarttravel.domain.model.ItineraryItem
import com.monarca.smarttravel.utils.AppError
import javax.inject.Inject

class ItineraryRepositoryImpl @Inject constructor() : ItineraryRepository {

    private val TAG = "ItineraryRepositoryImpl"
    private val dataSource = FakeItineraryItemDataSource

    override suspend fun getItemsByTrip(tripId: Int): List<ItineraryItem> {
        val result = dataSource.getItemsByTrip(tripId)
        Log.d(TAG, "getItemsByTrip: tripId=$tripId -> ${result.size} items")
        return result
    }

    override fun getItemById(id: Int): ItineraryItem? {
        val item = dataSource.getItemById(id)
        if (item == null) Log.w(TAG, "getItemById: no s'ha trobat id=$id")
        return item
    }

    override fun addItineraryItem(item: ItineraryItem): Int {
        val status = dataSource.addItem(item)
        if (status == AppError.OK.code) {
            Log.i(TAG, "addItineraryItem: creat -> tripId=${item.tripId}, tipus=${item.type}")
        } else {
            Log.e(TAG, "addItineraryItem: error inesperat -> status=$status, tipus=${item.type}")
        }
        return status
    }

    override fun updateItineraryItem(item: ItineraryItem): Int {
        val status = dataSource.updateItem(item)
        if (status == AppError.OK.code) {
            Log.i(TAG, "updateItineraryItem: actualitzat -> id=${item.id}, tipus=${item.type}")
        } else {
            Log.w(TAG, "updateItineraryItem: no s'ha trobat id=${item.id}")
        }
        return status
    }

    override fun deleteItineraryItem(id: Int): Int {
        val status = dataSource.deleteItem(id)
        if (status == AppError.OK.code) {
            Log.i(TAG, "deleteItineraryItem: eliminat -> id=$id")
        } else {
            Log.w(TAG, "deleteItineraryItem: no s'ha trobat id=$id")
        }
        return status
    }
}