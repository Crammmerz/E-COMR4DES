package com.android.inventorytracker.presentation.popup.batch_targeted_removal

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import com.android.inventorytracker.util.onSubUnitChange
import com.android.inventorytracker.util.onUnitChange
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Theme Colors copied for consistency
val PopupSurface = Color(0xFFFFF9F5)
val ButtonDarkBrown = Color(0xFF4A3B32)
val TextDarkBrown = Color(0xFF4A3B32)

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
    var dateValue by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.55f)
                .fillMaxHeight(0.65f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PopupSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp) // Padding for the content area
            ) {
                // 1. Content Column (Header + Inputs)
                // We constrain the height here to ensure it doesn't collide with the buttons.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f), // **FIX: Constrain height to reserve space (10%)**
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // --- Header ---
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Deduct Stock (Targeted)",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDarkBrown
                        )
                        Text(
                            "Specify the date and amount you wish to remove.",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // --- Input Fields ---
                    // Note: If you have a huge number of inputs, this inner column needs scrolling.
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DateField(
                            value = dateValue,
                            onValueChange = { dateValue = it },
                            onValidityChange = { /* optional */ },
                            onDone = { focusManager.clearFocus(force = true) },
                            header = "Expiry Date",
                            placeholder = "MM/DD/YYYY"
                        )

                        FloatField(
                            value = unit,
                            onValueChange = { value ->
                                onUnitChange(
                                    unit = value, threshold,
                                    onUnit = { unit = it },
                                    onSubUnit = { subUnit = it }
                                )
                            },
                            onValidityChange = { validUnit = it },
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
                            onValidityChange = { validUnit = it },
                            label = "Sub Unit",
                            placeholder = "Enter number of sub units",
                            doClear = true,
                        )
                    }
                }

                // 2. Buttons Row (Independent of Content Column's height)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd), // **FIX: Aligned to the absolute bottom**
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CancelButton(onClick = { onDismiss() })

                    ConfirmButton(
                        text = "Deduct Stock",
                        containerColor = ButtonDarkBrown,
                        onClick = {
                            val selectedDate = runCatching {
                                LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                            }.getOrNull()

                            val exist = selectedDate?.let { date ->
                                batch.firstOrNull {
                                    val batchDate = Instant.ofEpochMilli(it.expiryDate)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                    batchDate == date
                                }
                            }

                            when {
                                selectedDate == null || exist == null ->
                                    Toast.makeText(context, "Please enter a valid Date", Toast.LENGTH_SHORT).show()
                                !validUnit ->
                                    Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()
                                else -> {
                                    batchViewModel.onTargetedDeductStock(exist, subUnit)
                                    onDismiss()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}