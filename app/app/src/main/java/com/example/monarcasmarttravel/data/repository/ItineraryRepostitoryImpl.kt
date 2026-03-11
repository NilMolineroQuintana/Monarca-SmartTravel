package com.example.monarcasmarttravel.data.repository

import com.example.monarcasmarttravel.data.fakeDB.FakeItineraryItemDataSource
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import com.example.monarcasmarttravel.domain.interfaces.ItineraryItemRepository
import javax.inject.Inject

class ItineraryItemRepositoryImpl @Inject constructor() : ItineraryItemRepository {

    private val dataSource = FakeItineraryItemDataSource

    override suspend fun getItemsByTrip(tripId: Int): List<ItineraryItem> =
        dataSource.getItemsByTrip(tripId)

    override suspend fun addItineraryItem(item: ItineraryItem): Boolean =
        dataSource.addItem(item)

    override suspend fun updateItineraryItem(item: ItineraryItem): Boolean =
        dataSource.updateItem(item)

    override suspend fun deleteItineraryItem(id: Int): Boolean =
        dataSource.deleteItem(id)
}