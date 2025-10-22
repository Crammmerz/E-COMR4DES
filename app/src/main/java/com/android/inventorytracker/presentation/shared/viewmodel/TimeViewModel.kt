package com.android.inventorytracker.presentation.shared.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.model.TimeStampUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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

    fun LocalDateTime.toTimestamp(): String =
        format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    fun LocalDateTime.toDayAndDate(): String {
        val dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val monthName = month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return "$dayName, $monthName $dayOfMonth"
    }


    fun getTimestamp() = _currentTime.value.toTimestamp()
    fun getDayAndDate() = _currentTime.value.toDayAndDate()

    fun getDateMeta(): TimeStampUiModel {
        val now = _currentTime.value
        val dayName = now.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

        return TimeStampUiModel(
            time = now.toLocalTime(),
            day = now.dayOfWeek,
            dayName = dayName,
            month = now.month
        )
    }
}