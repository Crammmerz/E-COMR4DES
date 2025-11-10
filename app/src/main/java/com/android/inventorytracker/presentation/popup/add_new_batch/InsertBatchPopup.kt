package com.android.inventorytracker.presentation.popup.add_new_batch

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.add_new_batch.component.AddBatchButton
import com.android.inventorytracker.presentation.popup.add_new_batch.component.CancelButton
import com.android.inventorytracker.presentation.shared.component.item_property_fields.SubUnitField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.UnitFieldFloat
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InsertBatchPopup(
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
            Row (){
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
            DatePicker(state = datePickerState)
            Row {
                AddBatchButton {
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    val selectedDate = selectedDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }

                    val isTodayOrFuture = selectedDate != null && !selectedDate.isBefore(LocalDate.now())
                    if(!isTodayOrFuture)Toast.makeText(context, "Date is wrong", Toast.LENGTH_SHORT).show()
                    if(unit<=0)Toast.makeText(context, "Unit is wrong", Toast.LENGTH_SHORT).show()
                    if (isTodayOrFuture && unit > 0) {
                        val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                        val batch = ItemBatchEntity(
                            itemId = itemModel.item.id,
                            unit = unit,
                            expiryDate = formattedDate
                        )
                        onStore(batch)
                        Toast.makeText(context, "Item stored successfully", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                }
                CancelButton { onDismiss() }
            }
        }
    }
}
