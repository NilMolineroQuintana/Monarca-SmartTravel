package com.example.monarcasmarttravel.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.monarcasmarttravel.domain.model.ItineraryItem
import androidx.room.TypeConverters
import com.example.monarcasmarttravel.domain.model.AccesHistory
import com.example.monarcasmarttravel.domain.model.User
import com.example.monarcasmarttravel.utils.Converters

@Database(entities = [User::class, AccesHistory::class, ItineraryItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class MonarcaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun itineraryDao(): ItineraryDao
}