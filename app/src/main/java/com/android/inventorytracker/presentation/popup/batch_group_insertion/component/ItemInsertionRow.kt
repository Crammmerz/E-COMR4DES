package com.android.inventorytracker.presentation.popup.batch_group_insertion.component

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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.InsertBatch
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.shared.component.input_fields.DateField
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.LightSand
import com.android.inventorytracker.util.onSubUnitChange
import com.android.inventorytracker.util.onUnitChange
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ItemInsertionRow(
    modifier: Modifier = Modifier,
    model: ItemModel,
    isPersistent: Boolean,
    onValueChange: (InsertBatch) -> Unit,
    onValidityChange: (Boolean) -> Unit,
    itemViewModel: ItemViewModel = hiltViewModel(),
) {
    var unit by rememberSaveable { mutableFloatStateOf(0f) }
    var subUnit by rememberSaveable { mutableIntStateOf(0) }
    var dateValue by rememberSaveable { mutableStateOf("") }

    var validUnit by rememberSaveable { mutableStateOf(false) }
    var validDate by rememberSaveable { mutableStateOf(false) }
    val valid = validUnit && validDate

    val focusDate = remember { FocusRequester() }

    LaunchedEffect(valid) {
        if (isPersistent) onValidityChange(valid)
        if (valid) {
            val parsedDate = runCatching {
                LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            }.getOrNull()
            if (parsedDate != null) {
                onValueChange(
                    InsertBatch(
                        itemId = model.item.id,
                        itemName = model.item.name,
                        unit = unit,
                        subunit = subUnit,
                        expiryDate = parsedDate.atStartOfDay(ZoneId.systemDefault())
                            .toInstant().toEpochMilli()
                    )
                )
            }
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
        ){
            Checkbox(isPersistent, onCheckedChange = { itemViewModel.togglePersistence(model.item.id, it) })
            Column {
                Text(model.item.name)
                Row (horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(text = "Unit: ${model.totalUnitFormatted()}")
                    Text(text = "Nearest Expiry: ${model.nearestExpiryFormatted}")
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
                    label = "Unit",
                    placeholder = "Enter Unit",
                    value = unit,
                    onValueChange = { value ->
                        onUnitChange(
                            unit = value, model.item.subUnitThreshold,
                            onUnit = { unit = it },
                            onSubUnit = { subUnit = it }
                        )
                    },
                    onValidityChange = { validUnit = it },
                    onDone = { focusDate.requestFocus() }
                )
                IntField(
                    modifier = Modifier.weight(1f),
                    value = subUnit,
                    onValueChange = { value ->
                        onSubUnitChange(
                            subUnit = value, model.item.subUnitThreshold,
                            onUnit = { unit = it },
                            onSubUnit = { subUnit = it }
                        )
                    },
                    label = "Sub Unit",
                    placeholder = "Enter number of sub units",
                    onValidityChange = { validUnit = it },
                    onDone = { focusDate.requestFocus() },
                    doClear = true,
                )
                DateField(
                    modifier = Modifier.weight(1f),
                    value = dateValue,
                    onValueChange = { dateValue = it },
                    header = "Expiry Date",
                    placeholder = "MM/DD/YYYY",
                    onValidityChange = { isFormatValid ->
                        val parsedDate = runCatching {
                            LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                        }.getOrNull()

                        validDate = isFormatValid && parsedDate?.isAfter(LocalDate.now()) == true
                    },
                )
            }
        }
    }
}