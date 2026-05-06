package com.monarca.smarttravel.data.repository

import android.util.Log
import com.monarca.smarttravel.data.ImageDao
import com.monarca.smarttravel.domain.interfaces.ImageRepository
import com.monarca.smarttravel.domain.model.Image
import com.monarca.smarttravel.domain.model.ItineraryItem
import com.monarca.smarttravel.utils.AppError
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

    override suspend fun addImage(image: Image): Int {
        val id = imageDao.insertImage(image)
        return if (id > 0) AppError.OK.code else AppError.UNKNOWN.code
    }

    override suspend fun deleteImage(image: Image): Int {
        val affected = imageDao.deleteImage(image)
        return if (affected > 0) AppError.OK.code else AppError.UNKNOWN.code
    }
}