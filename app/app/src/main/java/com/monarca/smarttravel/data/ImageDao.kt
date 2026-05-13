package com.monarca.smarttravel.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.monarca.smarttravel.domain.model.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM images WHERE tripId = :tripId")
    fun getImagesByTrip(tripId: Int): Flow<List<Image>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image): Long

    @Delete
    suspend fun deleteImage(image: Image): Int

    @Query("DELETE FROM images WHERE tripId = :tripId")
    suspend fun deleteAllImagesFromTrip(tripId: Int): Int

}