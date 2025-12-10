package com.android.inventorytracker.presentation.popup.batch_targeted_removal

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.presentation.shared.component.input_fields.DateField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.util.onSubUnitChange
import com.android.inventorytracker.util.onUnitChange
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BatchTargetedRemoval(
    threshold: Int,
    batch: List<ItemBatchEntity>,
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    var unit by rememberSaveable { mutableFloatStateOf(0f) }
    var subUnit by rememberSaveable { mutableIntStateOf(0) }

    var validUnit by rememberSaveable { mutableStateOf(false) }
    var dateValue by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    DialogHost(
        Modifier
            .fillMaxHeight(0.8f)
            .fillMaxWidth(0.5f),
        onDismissRequest = onDismiss,
        useImePadding = true
    ) {
        Column {
            Text("Stock Deduction")

            DateField(
                value = dateValue,
                onValueChange = { dateValue = it },
                onValidityChange = { /* optional */ },
                onDone = { focusManager.clearFocus(force = true) },
                header = "Expiry Date",
                placeholder = "MM/DD/YYYY"
            )

            FloatField(
                value = unit,
                onValueChange = { value ->
                    onUnitChange(
                        unit = value, threshold,
                        onUnit = { unit = it },
                        onSubUnit = { subUnit = it }
                    )
                },
                onValidityChange = { validUnit = it },
                label = "Unit",
                placeholder = "Enter number of units",
            )

            IntField(
                value = subUnit,
                onValueChange = { value ->
                    onSubUnitChange(
                        subUnit = value, threshold,
                        onUnit = { unit = it },
                        onSubUnit = { subUnit = it }
                    )
                },
                onValidityChange = { validUnit = it },
                label = "Sub Unit",
                placeholder = "Enter number of sub units",
                doClear = true,
            )

            Row {
                CancelButton(onClick = { onDismiss() })
                ConfirmButton("Deduct Stock") {
                    val selectedDate = runCatching {
                        LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                    }.getOrNull()

                    // Convert batch expiryDate (Long) back to LocalDate for comparison
                    val exist = selectedDate?.let { date ->
                        batch.firstOrNull {
                            val batchDate = Instant.ofEpochMilli(it.expiryDate)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            batchDate == date
                        }
                    }

                    when {
                        selectedDate == null || exist == null ->
                            Toast.makeText(context, "Please enter a valid Date", Toast.LENGTH_SHORT).show()
                        !validUnit ->
                            Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()
                        else -> {
                            batchViewModel.onTargetedDeductStock(exist, subUnit)
                            onDismiss()
                        }
                    }
                }
            }
        }
    }
}