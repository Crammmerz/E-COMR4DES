package com.android.inventorytracker.presentation.popup.csv_removal.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.csv_removal.viewmodel.CSV
import com.android.inventorytracker.presentation.popup.csv_removal.viewmodel.CsvViewModel
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.ui.theme.LightSand
import com.android.inventorytracker.util.onSubUnitChange
import com.android.inventorytracker.util.onUnitChange

@Composable
fun DataRow(
    model: ItemModel,
    subUnit: Int,
    onUpdate: (CSV) -> Unit,
    onValidityChange: (Boolean) -> Unit,
    csvViewModel: CsvViewModel = hiltViewModel(),
){
    var unit by rememberSaveable { mutableFloatStateOf(0f) }
    var subUnit by rememberSaveable { mutableIntStateOf(subUnit) }

    var valid by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onSubUnitChange(
            subUnit = subUnit,
            threshold = model.item.subUnitThreshold,
            onUnit = { unit = it },
            onSubUnit = { subUnit = it }
        )
    }

    LaunchedEffect(valid) {
        if(valid){
            onUpdate(CSV(model.item.id, subUnit))
            onValidityChange(true)
        } else {
            onValidityChange(false)
        }
    }

    Column (Modifier.background(LightSand).padding(10.dp)) {
        Row {
            IconButton(onClick = {csvViewModel.removeData(model.item.id)}) {
                Icon(Icons.Default.DeleteOutline, tint = Color.Red, contentDescription = "Delete")
            }
            Column {
                Row {
                    Text(model.item.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "ID: ${model.item.id}")
                }

                Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Current: ${model.totalUnitFormatted()}")
                    Text(text = "Expiry: ${model.nearestExpiryFormatted}")
                }
            }
        }

        HorizontalDivider(Modifier.fillMaxWidth())

        Row (Modifier.fillMaxWidth()) {
            FloatField(
                modifier = Modifier.weight(1f),
                label = "Unit: max ${model.totalUnit()}",
                placeholder = "Enter Unit:",
                value = unit,
                valueRange = 0f..model.totalUnit().toFloat(),
                onValueChange = { value ->
                    onUnitChange(
                        unit = value,
                        threshold = model.item.subUnitThreshold,
                        onUnit = { unit = it },
                        onSubUnit = { subUnit = it }
                    )
                },
                onValidityChange = { valid = it && subUnit <= model.totalSubUnit() },
            )
            IntField(
                modifier = Modifier.weight(1f),
                label = "Sub Unit: max ${model.totalSubUnit()}",
                placeholder = "Enter sub unit",
                doClear = true,
                value = subUnit,
                valueRange = 1..model.totalSubUnit(),
                onValueChange = { value ->
                    onSubUnitChange(
                        subUnit = value,
                        threshold = model.item.subUnitThreshold,
                        onUnit = { unit = it },
                        onSubUnit = { subUnit = it }
                    )
                },
                onValidityChange = { valid = it },
            )
        }
    }
}