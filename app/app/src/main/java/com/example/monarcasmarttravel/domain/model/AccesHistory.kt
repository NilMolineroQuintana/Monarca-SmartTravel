package com.example.monarcasmarttravel.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity (
    tableName = "acces_history",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AccesHistory (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    @ColumnInfo(defaultValue = "(strftime('%s','now'))") val date: Long = System.currentTimeMillis()
)