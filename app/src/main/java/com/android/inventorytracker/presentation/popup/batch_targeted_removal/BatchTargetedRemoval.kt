package com.android.inventorytracker.presentation.popup.batch_targeted_removal

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.presentation.shared.component.item_property_fields.SubUnitField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.UnitFieldFloat
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost

@Composable
fun BatchTargetedRemoval(
    batch: List<ItemBatchEntity>,
    unit: Float,
    subUnit: Int,
    onUnitChange: (Float) -> Unit,
    onSubUnitChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onTargetedDeduct: (batch: ItemBatchEntity, toRemove: Int) -> Unit,
){
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
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
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.Transparent,
                    selectedDayContainerColor = Color(0xFF4CAF50),
                    todayContentColor = Color.Black
                )
            ) //TODO (Design of Date Picker)
            UnitFieldFloat(
                unit = unit, onUnitChange = onUnitChange, // Pass value handler
                Modifier.padding(top = 10.dp)
            )
            SubUnitField(
                subUnit = subUnit, onSubUnitChange = onSubUnitChange, // Pass value handler
                Modifier.padding(vertical = 10.dp)
            )
            Row {
                CancelButton(onClick = { onDismiss() })
                ConfirmButton("Deduct Stock") {
                    val date = datePickerState.selectedDateMillis
                    val exist = batch.firstOrNull { it.expiryDate == date }
                    when {
                        date == null || exist == null ->
                            Toast.makeText(context, "Please enter a valid Date", Toast.LENGTH_SHORT).show()
                        subUnit <= 0 ->
                            Toast.makeText(context, "Amount must be greater than 0", Toast.LENGTH_SHORT).show()
                        exist.subUnit < subUnit ->
                            Toast.makeText(context, "Amount exceeds available stock", Toast.LENGTH_SHORT).show()
                        else -> {
                            onTargetedDeduct(exist, subUnit)
                            onDismiss()
                        }
                    }
                }
            }
        }
    }
}