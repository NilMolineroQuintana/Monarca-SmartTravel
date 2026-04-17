package com.example.monarcasmarttravel.utils

import androidx.room.TypeConverter
import com.example.monarcasmarttravel.ui.screens.trip.PlanType
import java.util.Date

class Converters {

    // Date <-> Long
    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(millis: Long?): Date? = millis?.let { Date(it) }

    // PlanType <-> String
    @TypeConverter
    fun fromPlanType(type: PlanType?): String? = type?.name

    @TypeConverter
    fun toPlanType(name: String?): PlanType? = name?.let { PlanType.valueOf(it) }
}