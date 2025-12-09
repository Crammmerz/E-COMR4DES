package com.android.inventorytracker.presentation.popup.batch_removal

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.util.onSubUnitChange
import com.android.inventorytracker.util.onUnitChange

@Composable
fun BatchDeductPopup(
    threshold: Int,
    batch: List<ItemBatchEntity>,
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    var unit by rememberSaveable { mutableFloatStateOf(0f) }
    var subUnit by rememberSaveable { mutableIntStateOf(0) }

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
                value = unit,
                onValueChange = { value ->
                    onUnitChange(
                        unit = value, threshold,
                        onUnit = { unit = it },
                        onSubUnit = { subUnit = it }
                    )
                },
                onValidityChange = {},
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
                onValidityChange = {},
                label = "Sub Unit",
                placeholder = "Enter number of sub units",
                doClear = true,
            )
            Row {
                CancelButton(onClick = { onDismiss() },)
                ConfirmButton("Deduct Stock") {
                    when  {
                        subUnit > batch.sumOf { it.subUnit } || subUnit <= 0 ->
                            Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                        else ->
                            batchViewModel.onDeductStock(batch, subUnit)
                    }
                }
            }
        }
    }
}