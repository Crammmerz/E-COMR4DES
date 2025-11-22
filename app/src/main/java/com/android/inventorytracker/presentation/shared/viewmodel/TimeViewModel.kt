package com.android.inventorytracker.presentation.shared.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.model.TimeStampUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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
            while (isActive) {
                delay(1000)
                _currentTime.value = LocalDateTime.now()
            }
        }
    }
    fun getDayAndDate(): String = _currentTime.value.toDayAndDate()
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toDayAndDate(): String {
    val dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val monthName = month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    return "$dayName, $monthName $dayOfMonth"
}
