package com.android.inventorytracker.presentation.popup.csv_removal.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.csv_removal.viewmodel.CSV
import com.android.inventorytracker.presentation.popup.csv_removal.viewmodel.CsvViewModel
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.util.onSubUnitChange
import com.android.inventorytracker.util.onUnitChange

@Composable
fun DataRow(
    model: ItemModel,
    subUnit: Int,
    onUpdate: (CSV) -> Unit,
    onValidityChange: (Boolean) -> Unit,
    csvViewModel: CsvViewModel = hiltViewModel(),
) {
    var unitState by rememberSaveable { mutableFloatStateOf(0f) }
    var subUnitState by rememberSaveable { mutableIntStateOf(subUnit) }
    var valid by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onSubUnitChange(
            subUnit = subUnitState,
            threshold = model.item.subUnitThreshold,
            onUnit = { unitState = it },
            onSubUnit = { subUnitState = it }
        )
    }

    LaunchedEffect(valid) {
        if (valid) {
            onUpdate(CSV(model.item.id, subUnitState))
            onValidityChange(true)
        } else {
            onValidityChange(false)
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        tonalElevation = 1.dp,
        border = if (!valid) null else null // Optional: Add a subtle green border if valid
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Section: Info and Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = model.item.name,
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Stock: ${model.totalUnitFormatted()}",
                            style = TextStyle(
                                fontFamily = GoogleSans,
                                fontSize = 12.sp,
                                color = Palette.ButtonDarkBrown.copy(alpha = 0.8f)
                            )
                        )
                        Text(
                            text = "Expiry: ${model.nearestExpiryFormatted}",
                            style = TextStyle(
                                fontFamily = GoogleSans,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        )
                    }
                }

                IconButton(
                    onClick = { csvViewModel.removeData(model.item.id) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Remove",
                        tint = Color.Red.copy(alpha = 0.6f)
                    )
                }
            }

            HorizontalDivider(
                thickness = 0.5.dp,
                color = Color.LightGray.copy(alpha = 0.3f)
            )

            // Bottom Section: Input Fields
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FloatField(
                    modifier = Modifier.weight(1f),
                    label = "Unit (Max: ${model.totalUnit()})",
                    placeholder = "0",
                    value = unitState,
                    valueRange = 0f..model.totalUnit().toFloat(),
                    onValueChange = { value ->
                        onUnitChange(
                            unit = value,
                            threshold = model.item.subUnitThreshold,
                            onUnit = { unitState = it },
                            onSubUnit = { subUnitState = it }
                        )
                    },
                    onValidityChange = { valid = it && subUnitState <= model.totalSubUnit() },
                )

                IntField(
                    modifier = Modifier.weight(1f),
                    label = "Sub Unit (Max: ${model.totalSubUnit()})",
                    placeholder = "0",
                    doClear = true,
                    value = subUnitState,
                    valueRange = 1..model.totalSubUnit(),
                    onValueChange = { value ->
                        onSubUnitChange(
                            subUnit = value,
                            threshold = model.item.subUnitThreshold,
                            onUnit = { unitState = it },
                            onSubUnit = { subUnitState = it }
                        )
                    },
                    onValidityChange = { valid = it },
                )
            }
        }
    }
}