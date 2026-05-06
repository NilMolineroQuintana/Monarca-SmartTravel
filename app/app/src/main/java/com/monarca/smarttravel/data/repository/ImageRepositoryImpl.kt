package com.monarca.smarttravel.data.repository

import android.util.Log
import com.monarca.smarttravel.data.ImageDao
import com.monarca.smarttravel.domain.interfaces.ImageRepository
import com.monarca.smarttravel.domain.model.Image
import com.monarca.smarttravel.domain.model.ItineraryItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageDao: ImageDao
) : ImageRepository {

    private val TAG = "ImageRepositoryImpl"

    override fun getImagesByTrip(tripId: Int): Flow<List<Image>> {
        Log.d(TAG, "getItemsByTrip: observant tripId=$tripId")
        return imageDao.getImagesByTrip(tripId)
    }
}