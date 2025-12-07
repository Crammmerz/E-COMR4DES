package com.android.inventorytracker.presentation.popup.batch_removal

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost

@Composable
fun BatchDeductPopup(
    batch: List<ItemBatchEntity>,
    unit: Float,
    subUnit: Int,
    onUnitChange: (Float) -> Unit,
    onSubUnitChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onDeductStock: (List<ItemBatchEntity>, toRemove: Int) -> Unit
    ,
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
            Text("Stock Deduction")
            FloatField(
                value = unit, onValueChange = onUnitChange,
                header = "Unit",
                placeholder = "Enter number of units",
                isValid = {}
            )
            IntField(
                value = subUnit,
                onValueChange = onSubUnitChange,
                header = "Sub Unit",
                placeholder = "Enter number of sub units",
                doClear = true,
                isValid = {}
            )
            Row {
                CancelButton(onClick = { onDismiss() },)
                ConfirmButton("Deduct Stock") {
                    when  {
                        subUnit > batch.sumOf { it.subUnit } || subUnit <= 0 ->
                            Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                        else ->
                            onDeductStock(batch, subUnit)
                    }
                }
            }
        }
    }
}