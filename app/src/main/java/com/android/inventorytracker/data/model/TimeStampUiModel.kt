package com.android.inventorytracker.data.model

import java.time.DayOfWeek
import java.time.LocalTime
import java.time.Month

data class TimeStampUiModel(
    val time: LocalTime,
    val day: DayOfWeek,
    val dayName: String,
    val month: Month
)