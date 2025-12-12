package com.android.inventorytracker.presentation.popup.batch_group_removal.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.RemoveBatch
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.LightSand
import com.android.inventorytracker.util.onSubUnitChange
import com.android.inventorytracker.util.onUnitChange

@Composable
fun ItemRemovalRow(
    modifier: Modifier = Modifier,
    model: ItemModel,
    isPersistent: Boolean,
    onValueChange: (RemoveBatch) -> Unit,
    onValidityChange: (Boolean) -> Unit,
    itemViewModel: ItemViewModel = hiltViewModel(),
) {
    var unit by rememberSaveable { mutableFloatStateOf(0f) }
    var subUnit by rememberSaveable { mutableIntStateOf(0) }

    var valid by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(valid) {
        if (isPersistent) onValidityChange(valid)
        if (valid) {
            onValueChange(
                RemoveBatch(
                    itemId = model.item.id,
                    itemName = model.item.name,
                    batches = model.batch,
                    unit = unit,
                    subunit = subUnit
                )
            )
        }
    }

    Column(modifier = modifier
        .background(LightSand)
        .fillMaxWidth()
    ) {
        Row (
            modifier = Modifier
                .padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(isPersistent, onCheckedChange = { itemViewModel.togglePersistence(model.item.id, it) })
            Column {
                Text(model.item.name)
                Row (horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Current: ${model.totalUnitFormatted()}")
                    Text(text = "Expiry: ${model.nearestExpiryFormatted}")
                }
            }
        }

        if(isPersistent){
            HorizontalDivider(Modifier.fillMaxWidth())
            Row (
                modifier = Modifier
                    .padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.Top
            ) {
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
                    placeholder = "Enter number of sub units",
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
                    doClear = true,
                )
            }
        }
    }
}