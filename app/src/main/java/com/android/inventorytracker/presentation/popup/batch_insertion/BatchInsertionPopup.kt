package com.android.inventorytracker.presentation.popup.batch_insertion

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.shared.component.input_fields.DateField
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette
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

    val focusUnit = remember { FocusRequester() }
    val focusSubUnit = remember { FocusRequester() }
    val focusDate = remember { FocusRequester() }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        focusDate.requestFocus()
    }

    fun doSubmit() {
        val selectedDate = runCatching {
            LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        }.getOrNull()

        if (selectedDate == null) {
            Toast.makeText(context, "Please enter a valid date", Toast.LENGTH_SHORT).show()
        } else if (!validUnit) {
            Toast.makeText(context, "Please enter valid unit/subunit", Toast.LENGTH_SHORT).show()
        } else {
            val batch = ItemBatchEntity(
                itemId = itemModel.item.id,
                subUnit = subUnit,
                expiryDate = selectedDate
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            )
            batchViewModel.onStoreBatch(batch)
            onDismiss()
            Toast.makeText(context, "Stock Added!", Toast.LENGTH_SHORT).show()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(480.dp)
                .heightIn(max = 620.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Header (Exact copy of Removal design)
                Text(
                    text = "Add Stock",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Palette.ButtonDarkBrown
                    )
                )
                Text(
                    text = "Specify the expiry date and amount you wish to add.",
                    style = TextStyle(fontFamily = GoogleSans, fontSize = 13.sp, color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Scrollable Content Area
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DateField(
                        header = "Expiry Date",
                        placeholder = "MM/DD/YYYY",
                        value = dateValue,
                        onValueChange = { dateValue = it },
                        onValidityChange = { isFormatValid ->
                            // Check if the current dateValue is parseable
                            val parsed = runCatching {
                                LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                            }.getOrNull()
                            validDate = isFormatValid && parsed?.isAfter(LocalDate.now()) == true
                        },
                        validateAfterToday = true,
                        onDone = { focusUnit.requestFocus() }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FloatField(
                            modifier = Modifier.weight(1f),
                            label = "Unit",
                            placeholder = "0",
                            value = unit,
                            onValueChange = { value ->
                                onUnitChange(
                                    unit = value,
                                    threshold = itemModel.item.subUnitThreshold,
                                    onUnit = { unit = it },
                                    onSubUnit = { subUnit = it }
                                )
                            },
                            onValidityChange = { validUnit = it },
                            onDone = { focusSubUnit.requestFocus() }
                        )

                        IntField(
                            modifier = Modifier.weight(1f),
                            label = "Sub Unit",
                            placeholder = "0",
                            doClear = true,
                            value = subUnit,
                            onValueChange = { value ->
                                onSubUnitChange(
                                    subUnit = value,
                                    threshold = itemModel.item.subUnitThreshold,
                                    onUnit = { unit = it },
                                    onSubUnit = { subUnit = it }
                                )
                            },
                            onValidityChange = { validUnit = it },
                            onDone = { doSubmit() }
                        )
                    }
                }

                // Footer (Static)
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CancelButton(onClick = onDismiss)
                    ConfirmButton(
                        text = "Add Stock",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = validUnit && validDate,
                        onClick = { doSubmit() }
                    )
                }
            }
        }
    }
}