package com.monarca.smarttravel.data.repository

import com.monarca.smarttravel.data.ImageDao
import com.monarca.smarttravel.domain.interfaces.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageDao: ImageDao
) : ImageRepository {

}