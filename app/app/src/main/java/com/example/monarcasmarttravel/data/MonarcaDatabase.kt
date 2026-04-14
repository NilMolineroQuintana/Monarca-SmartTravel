package com.example.monarcasmarttravel.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.monarcasmarttravel.domain.model.User

@Database(entities = [User::class], version = 1)
abstract class MonarcaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}