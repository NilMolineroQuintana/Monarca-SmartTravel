package com.monarca.smarttravel.data.repository

import android.util.Log
import com.monarca.smarttravel.data.fakeDB.FakeItineraryItemDataSource
import com.monarca.smarttravel.domain.interfaces.ItineraryRepository
import com.monarca.smarttravel.domain.model.ItineraryItem
import com.monarca.smarttravel.utils.AppError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItineraryRepositoryImpl @Inject constructor(
    private val dao: ItineraryDao
) : ItineraryRepository {

    private val TAG = "ItineraryRepositoryImpl"

    override fun getItemsByTrip(tripId: Int): Flow<List<ItineraryItem>> {
        Log.d(TAG, "getItemsByTrip: observant tripId=$tripId")
        return dao.getItemsByTrip(tripId)
    }

    override suspend fun getItemById(id: Int): ItineraryItem? {
        val item = dao.getItemById(id)
        if (item == null) Log.w(TAG, "getItemById: no s'ha trobat id=$id")
        return item
    }

    override suspend fun addItineraryItem(item: ItineraryItem): Int {
        val id = dao.addItem(item)
        return if (id > 0) AppError.OK.code else AppError.UNKNOWN.code
    }

    override suspend fun updateItineraryItem(item: ItineraryItem): Int {
        val affected = dao.updateItem(item)
        return if (affected > 0) AppError.OK.code else AppError.NON_EXISTING_ITEM.code
    }

    override suspend fun deleteItineraryItem(id: Int): Int {
        val affected = dao.deleteItem(id)
        return if (affected > 0) AppError.OK.code else AppError.NON_EXISTING_ITEM.code
    }
}