package com.android.inventorytracker.data.local.converter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate

class TypeConverters {
    @TypeConverter
    fun fromLocalDate(date : LocalDate): String{
        return date.toString()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(date: String) : LocalDate{
        return LocalDate.parse(date)
    }
}