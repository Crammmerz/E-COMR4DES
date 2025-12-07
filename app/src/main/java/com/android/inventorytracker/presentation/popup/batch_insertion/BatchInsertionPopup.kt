package com.android.inventorytracker.presentation.popup.batch_insertion

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.util.toLocalDate
import java.time.LocalDate

@Composable
fun BatchInsertionPopup(
    itemModel: ItemModel,
    unit: Float,
    subUnit: Int,
    onUnitChange: (Float) -> Unit,
    onSubUnitChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onStore: (ItemBatchEntity) -> Unit,
) {
    var validUnit by rememberSaveable { mutableStateOf(false) }
    var validSubUnit by rememberSaveable { mutableStateOf(false) }


    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    DialogHost(
        Modifier
            .fillMaxHeight(0.8f)
            .fillMaxWidth(0.5f),
        onDismissRequest = onDismiss,
        useImePadding = true
    ) {
        Column{
            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxSize(0.5f),
                colors = DatePickerDefaults.colors(
                    containerColor = Color.Transparent,
                    selectedDayContainerColor = Color(0xFF4CAF50),
                    todayContentColor = Color.Black
                )
            ) // TODO (Date Picker Design)

            FloatField(
                value = unit, onValueChange = onUnitChange,
                header = "Unit",
                placeholder = "Enter number of units",
                isValid = { validUnit = it },
                onDone = { focusManager.clearFocus(force = true) }
            )

            IntField(
                value = subUnit, onValueChange = onSubUnitChange,
                header = "Sub Unit",
                placeholder = "Enter number of sub units",
                doClear = true,
                isValid = { validSubUnit = it },
                onDone = { focusManager.clearFocus(force = true) }
            )
            Row {
                CancelButton(onClick = { onDismiss() },)
                ConfirmButton("Add Stock") {
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    val selectedDate = selectedDateMillis?.toLocalDate()

                    when {
                        selectedDate == null || selectedDate.isBefore(LocalDate.now()) ->
                            Toast.makeText(context, "Please enter a valid date", Toast.LENGTH_SHORT).show()
                        !validUnit && !validSubUnit ->
                            Toast.makeText(context, "Please enter a valid unit", Toast.LENGTH_SHORT).show()
                        else -> {
                            val batch = ItemBatchEntity(
                                itemId = itemModel.item.id,
                                subUnit = subUnit,
                                expiryDate = selectedDateMillis
                            )
                            onStore(batch)
                            onDismiss()
                        }
                    }
                }
            }
        }
    }
}
