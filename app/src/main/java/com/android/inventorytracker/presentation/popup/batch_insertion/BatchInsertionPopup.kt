package com.android.inventorytracker.presentation.popup.batch_insertion

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.shared.component.item_property_fields.SubUnitField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.UnitFieldFloat
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
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    val context = LocalContext.current

    DialogHost(
        Modifier
            .fillMaxHeight(0.8f)
            .fillMaxWidth(0.5f),
        onDismissRequest = onDismiss,
        useImePadding = true
    ) {
        Column{
            Row {
                Column(Modifier.weight(1f)) {
                    UnitFieldFloat(
                        unit = unit, onUnitChange = onUnitChange, // Pass value handler
                        Modifier.padding(top = 10.dp)
                    )
                }
                Column(Modifier.weight(1f)) {
                    SubUnitField(
                        subUnit = subUnit, onSubUnitChange = onSubUnitChange, // Pass value handler
                        Modifier.padding(vertical = 10.dp)
                    )
                }
            }
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.Transparent,
                    selectedDayContainerColor = Color(0xFF4CAF50),
                    todayContentColor = Color.Black
                )
            ) // TODO (Date Picker Design)
            Row {
                CancelButton(onClick = { onDismiss() },)
                ConfirmButton("Add Stock") {
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    val selectedDate = selectedDateMillis?.toLocalDate()

                    when {
                        selectedDate == null || selectedDate.isBefore(LocalDate.now()) ->
                            Toast.makeText(context, "Please enter a valid date", Toast.LENGTH_SHORT).show()
                        subUnit <= 0 ->
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
