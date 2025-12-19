package com.android.inventorytracker.presentation.popup.batch_targeted_removal

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.presentation.shared.component.input_fields.DateField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.util.onSubUnitChange
import com.android.inventorytracker.util.onUnitChange
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BatchTargetedRemoval(
    threshold: Int,
    batch: List<ItemBatchEntity>,
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    var unit by rememberSaveable { mutableFloatStateOf(0f) }
    var subUnit by rememberSaveable { mutableIntStateOf(0) }
    var validUnit by rememberSaveable { mutableStateOf(false) }
    var validDate by rememberSaveable { mutableStateOf(false) }
    var dateValue by rememberSaveable { mutableStateOf("") }

    // ✅ Kailangan ito para sa scroll
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(480.dp)
                .heightIn(max = 620.dp), // ✅ Max height para hindi lumampas sa screen
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Header (Static)
                Text(
                    text = "Deduct Stock",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Palette.ButtonDarkBrown
                    )
                )
                Text(
                    text = "Specify the date and amount you wish to remove.",
                    style = TextStyle(fontFamily = GoogleSans, fontSize = 13.sp, color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ✅ Scrollable Content Area
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false) // ✅ Ito ang magic para maging slim pero scrollable
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DateField(
                        header = "Expiry Date",
                        placeholder = "MM/DD/YYYY",
                        value = dateValue,
                        onValueChange = { dateValue = it },
                        onValidityChange = { validDate = it }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FloatField(
                            modifier = Modifier.weight(1f),
                            label = "Unit",
                            placeholder = "0",
                            value = unit,
                            onValueChange = { value ->
                                onUnitChange(value, threshold, { unit = it }, { subUnit = it })
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
                                onSubUnitChange(value, threshold, { unit = it }, { subUnit = it })
                            },
                            onValidityChange = { validUnit = it }
                        )
                    }

                    // Kung may balak kang magdagdag pa ng items dito, kusa na siyang mag-i-scroll.
                }

                // Footer (Static/Fixed at the bottom)
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
                        text = "Deduct Stock",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = validUnit && validDate, // Mas maganda kung disabled hangga't walang input
                        onClick = {
                            val selectedDate = runCatching {
                                LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                            }.getOrNull()

                            val exist = selectedDate?.let { date ->
                                batch.firstOrNull {
                                    Instant.ofEpochMilli(it.expiryDate).atZone(ZoneId.systemDefault()).toLocalDate() == date
                                }
                            }

                            if (selectedDate == null || exist == null) {
                                Toast.makeText(context, "No batch found for this date", Toast.LENGTH_SHORT).show()
                            } else {
                                batchViewModel.onTargetedDeductStock(exist, subUnit)
                                onDismiss()
                                Toast.makeText(context, "Stock Deducted!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}