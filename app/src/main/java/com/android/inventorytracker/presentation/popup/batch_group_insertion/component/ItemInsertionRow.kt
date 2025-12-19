package com.android.inventorytracker.presentation.popup.batch_group_insertion.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.InsertBatch
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.shared.component.input_fields.DateField
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans
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

    LaunchedEffect(valid, isPersistent) {
        if (isPersistent) onValidityChange(valid)
        if (valid && isPersistent) {
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
                        expiryDate = parsedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    )
                )
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isPersistent) Color.White else Color.Transparent,
        border = if (isPersistent) androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)) else null
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isPersistent,
                    onCheckedChange = { itemViewModel.togglePersistence(model.item.id, it) },
                    colors = CheckboxDefaults.colors(checkedColor = Palette.ButtonDarkBrown)
                )

                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = model.item.name,
                        style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Palette.ButtonDarkBrown)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Stock: ${model.totalUnitFormatted()}",
                            style = TextStyle(fontFamily = GoogleSans, fontSize = 12.sp, color = Color.Gray)
                        )
                        Text(
                            text = "Expiry: ${model.nearestExpiryFormatted}",
                            style = TextStyle(fontFamily = GoogleSans, fontSize = 12.sp, color = Color.Gray)
                        )
                    }
                }
            }

            if (isPersistent) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FloatField(
                        modifier = Modifier.weight(1f),
                        label = "Unit",
                        placeholder = "0",
                        value = unit,
                        onValueChange = { value ->
                            onUnitChange(value, model.item.subUnitThreshold, { unit = it }, { subUnit = it })
                        },
                        onValidityChange = { validUnit = it }
                    )
                    IntField(
                        modifier = Modifier.weight(1f),
                        label = "Sub Unit",
                        placeholder = "0",
                        doClear = true,
                        value = subUnit,
                        onValueChange = { value ->
                            onSubUnitChange(value, model.item.subUnitThreshold, { unit = it }, { subUnit = it })
                        },
                        onValidityChange = { validUnit = it }
                    )
                    DateField(
                        modifier = Modifier.weight(1.2f), // Give date more space
                        header = "Expiry",
                        placeholder = "MM/DD/YYYY",
                        value = dateValue,
                        onValueChange = { dateValue = it },
                        onValidityChange = { isFormatValid ->
                            val parsedDate = runCatching { LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy")) }.getOrNull()
                            validDate = isFormatValid && parsedDate?.isAfter(LocalDate.now()) == true
                        }
                    )
                }
            }
        }
    }
}