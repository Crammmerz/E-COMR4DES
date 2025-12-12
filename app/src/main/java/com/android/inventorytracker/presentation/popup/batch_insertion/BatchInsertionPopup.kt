package com.android.inventorytracker.presentation.popup.batch_insertion

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.shared.component.input_fields.DateField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.util.onSubUnitChange
import com.android.inventorytracker.util.onUnitChange
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BatchInsertionPopup(
    itemModel: ItemModel,
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    var unit by rememberSaveable { mutableFloatStateOf(0f) }
    var subUnit by rememberSaveable { mutableIntStateOf(0) }
    var dateValue by rememberSaveable { mutableStateOf("") }

    var validUnit by rememberSaveable { mutableStateOf(false) }
    var validDate by rememberSaveable { mutableStateOf(false) }
    val valid = validDate && validUnit

    var onSubmit by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(onSubmit) {
        if(valid){
            val selectedDate = runCatching {
                LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            }.getOrNull()
            if (selectedDate != null) {
                val batch = ItemBatchEntity(
                    itemId = itemModel.item.id,
                    subUnit = subUnit,
                    expiryDate = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                        .toEpochMilli()
                )
                batchViewModel.onStoreBatch(batch)
                onDismiss()
            }
        }
    }
    DialogHost(
        Modifier
            .fillMaxHeight(0.8f)
            .fillMaxWidth(0.5f),
        onDismissRequest = onDismiss,
        useImePadding = true
    ) {
        Column {
            DateField(
                value = dateValue,
                onValueChange = { dateValue = it },
                onValidityChange = { isFormatValid ->
                    val parsedDate = runCatching {
                        LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                    }.getOrNull()
                    validDate = isFormatValid && parsedDate?.isAfter(LocalDate.now()) == true
                },
                onDone = { focusManager.clearFocus(force = true) },
                header = "Expiry Date",
                placeholder = "MM/DD/YYYY"
            )

            FloatField(
                value = unit,
                onValueChange = { value ->
                    onUnitChange(
                        unit = value, itemModel.item.subUnitThreshold,
                        onUnit = { unit = it },
                        onSubUnit = { subUnit = it }
                    )
                },
                onValidityChange = { validUnit = it },
                label = "Unit",
                placeholder = "Enter number of units",
                onDone = { focusManager.clearFocus(force = true) }
            )

            IntField(
                value = subUnit,
                onValueChange = { value ->
                    onSubUnitChange(
                        subUnit = value, itemModel.item.subUnitThreshold,
                        onUnit = { unit = it },
                        onSubUnit = { subUnit = it }
                    )
                },
                label = "Sub Unit",
                placeholder = "Enter number of sub units",
                onValidityChange = { validUnit = it },
                onDone = { focusManager.clearFocus(force = true) },
                doClear = true,
            )

            Row {
                CancelButton(onClick = { onDismiss() })
                ConfirmButton("Add Stock") {
                    when {
                        !validDate ->
                            Toast.makeText(context, "Please enter a valid date", Toast.LENGTH_SHORT).show()
                        !validUnit ->
                            Toast.makeText(context, "Please enter valid unit/subunit", Toast.LENGTH_SHORT).show()
                        valid -> onSubmit = true
                    }
                }
            }
        }
    }
}
