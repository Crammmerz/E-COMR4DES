package com.android.inventorytracker.presentation.popup.expiryCalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.shared.component.SortDropdownMenu
import com.android.inventorytracker.presentation.shared.component.input_fields.SearchField
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.Palette
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.collections.toSet

@Composable
fun ExpiryCalendar(
    itemViewModel: ItemViewModel,
    onDismiss: () -> Unit
){
    val itemModels by itemViewModel.itemModelList.collectAsState()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .width(900.dp)
                .height(620.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Row(modifier = Modifier.focusable(false), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SearchField(Modifier.weight(1f))
                SortDropdownMenu()
            }
            CalendarContent(itemModels, setDatePicker = { selectedDate = it })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarContent(
    expiryItems: List<ItemModel>,
    setDatePicker: (LocalDate) -> Unit = {}
) {

    val datePickerState = rememberDatePickerState(
//        selectableDates = object : SelectableDates {
//            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
//                // Now we simply check if the calendar's day is in our Expiry Set
//                return expiryDaysSet.contains(utcTimeMillis)
//            }
//        }
    )

    // Sync with your external setter
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val date = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault()) // Convert back to local for the app logic
                .toLocalDate()
            setDatePicker(date)
        }
    }

    DatePicker(
        state = datePickerState,
        colors = DatePickerDefaults.colors(
            // All "Selectable" (Expiry) days will be Red
            dayContentColor = Color.Red,
            selectedDayContainerColor = Color.Red,

            // All "Non-Selectable" (No Expiry) days will be dimmed
            disabledDayContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    )
}