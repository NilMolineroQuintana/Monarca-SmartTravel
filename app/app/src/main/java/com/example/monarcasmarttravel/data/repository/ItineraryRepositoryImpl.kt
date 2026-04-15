package com.example.monarcasmarttravel.data.repository

import android.util.Log
import com.example.monarcasmarttravel.data.ItineraryDao
import com.example.monarcasmarttravel.data.fakeDB.FakeItineraryItemDataSource
import com.example.monarcasmarttravel.domain.interfaces.ItineraryRepository
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.utils.AppError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItineraryRepositoryImpl @Inject constructor(
    private val dao: ItineraryDao
) : ItineraryRepository {

    private val TAG = "ItineraryRepositoryImpl"
    private val dataSource = FakeItineraryItemDataSource

    override fun getItemsByTrip(tripId: Int): Flow<List<ItineraryItem>> {
        Log.d(TAG, "getItemsByTrip: observant tripId=$tripId")
        return dao.getItemsByTrip(tripId)
    }

    override fun getItemById(id: Int): ItineraryItem? {
        val item = dataSource.getItemById(id)
        if (item == null) Log.w(TAG, "getItemById: no s'ha trobat id=$id")
        return item
    }

    override suspend fun addItineraryItem(item: ItineraryItem): Int {
        val id = dao.addItem(item)
        return if (id > 0) AppError.OK.code else AppError.UNKNOWN.code
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

    override suspend fun deleteItineraryItem(id: Int): Int {
        val affected = dao.deleteItem(id)
        return if (affected > 0) AppError.OK.code else AppError.NON_EXISTING_ITEM.code
    }
}