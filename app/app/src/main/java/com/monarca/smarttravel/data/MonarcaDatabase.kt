package com.monarca.smarttravel.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.monarca.smarttravel.domain.model.AccesHistory
import com.monarca.smarttravel.domain.model.User
import com.monarca.smarttravel.utils.Converters

@Database(entities = [User::class, AccesHistory::class], version = 1)
@TypeConverters(Converters::class)
abstract class MonarcaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}