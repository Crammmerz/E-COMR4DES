package com.android.inventorytracker.presentation.popup.batch_removal

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.presentation.shared.component.item_property_fields.SubUnitField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.UnitFieldFloat
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost

@Composable
fun DeleteBatchPopup(
    totalSubUnit: Int,
    batch: List<ItemBatchEntity>,
    unit: Float,
    subUnit: Int,
    onUnitChange: (Float) -> Unit,
    onSubUnitChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onDelete: (List<ItemBatchEntity>, Int) -> Unit,
){
    val context = LocalContext.current
    DialogHost(
        Modifier
            .fillMaxHeight(0.8f)
            .fillMaxWidth(0.5f),
        onDismissRequest = onDismiss,
        useImePadding = true
    ) {
        Column {
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
            CancelButton(onClick = { onDismiss() },)
            ConfirmButton("Deduct Stock") {
                if(subUnit<=0){
                    Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                }
                if(subUnit>totalSubUnit){
                    Toast.makeText(context, "Not enough stock available", Toast.LENGTH_SHORT).show()
                }
                if (subUnit > 0 && subUnit <= totalSubUnit) {
                    onDelete(batch, subUnit)
                    onDismiss()
                }
            }
        }
    }
}