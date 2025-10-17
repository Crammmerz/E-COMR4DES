package com.android.inventorytracker.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter

data class TimeStampUiModel(
    val time: LocalTime,
    val day: DayOfWeek,
    val month: Month
)

@RequiresApi(Build.VERSION_CODES.O)
class TimeViewModel : ViewModel() {
    private val _currentTime = mutableStateOf(LocalDateTime.now())
    val currentTime: State<LocalDateTime> = _currentTime

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _currentTime.value = LocalDateTime.now()
            }
        }
    }

    fun getTimestamp(): String {
        return _currentTime.value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    fun getDateMeta(): TimeStampUiModel {
        val now = _currentTime.value
        return TimeStampUiModel(
            time = now.toLocalTime(),
            day = now.dayOfWeek,
            month = now.month
        )
    }
}