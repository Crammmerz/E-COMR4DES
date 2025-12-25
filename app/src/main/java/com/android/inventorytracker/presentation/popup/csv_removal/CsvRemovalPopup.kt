package com.android.inventorytracker.presentation.popup.csv_removal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.popup.csv_removal.component.DataRow
import com.android.inventorytracker.presentation.popup.csv_removal.viewmodel.CsvViewModel
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans

@Composable
fun CsvRemovalPopup(
    csvViewModel: CsvViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val model = csvViewModel.model.collectAsState(initial = emptyList())
    val data = csvViewModel.data.observeAsState(emptyList())
    val dropped = csvViewModel.droppedCount.observeAsState()
    var validityMap by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }
    var valid by remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun onConfirm() {
        valid = data.value.all { row -> validityMap[row.id] == true }

        if (valid) {
            data.value.forEach { row ->
                val matchedModel = model.value.firstOrNull { it.item.id == row.id }
                matchedModel?.let {
                    batchViewModel.onDeductStock(it.batch, row.subunit)
                }
            }
            csvViewModel.clearData()
            onDismiss()
        } else {
            Toast.makeText(
                context,
                "Oops! Looks like something’s missing — select an item or fill in the required fields.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    DialogHost(
        onDismissRequest = {
            csvViewModel.clearData()
            onDismiss()
        },
        useImePadding = true
    ) {
        Card(
            modifier = Modifier
                .width(600.dp)
                .heightIn(max = 700.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header section
                Text(
                    text = "CSV Deduction Preview",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Palette.DarkBeigeText
                    )
                )

                if (dropped.value != null && dropped.value != 0) {
                    Text(
                        text = "Dropped ${dropped.value} unrecognized items",
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontSize = 12.sp,
                            color = Color.Red.copy(alpha = 0.8f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Content Area with LazyColumn
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (data.value.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = data.value,
                                key = { row -> row.id }
                            ) { row ->
                                val matchedModel = model.value.firstOrNull { it.item.id == row.id }
                                if (matchedModel != null) {
                                    DataRow(
                                        model = matchedModel,
                                        subUnit = row.subunit,
                                        onUpdate = csvViewModel::updateData,
                                        onValidityChange = { isValid ->
                                            validityMap = validityMap + (matchedModel.item.id to isValid)
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No valid data detected in CSV",
                                style = TextStyle(
                                    fontFamily = GoogleSans,
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }

                // Footer section
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "${data.value.size} items ready for deduction",
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Applying Google Sans to Cancel and Confirm Buttons
                    CompositionLocalProvider(
                        LocalTextStyle provides TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Medium)
                    ) {
                        CancelButton {
                            csvViewModel.clearData()
                            onDismiss()
                        }

                        ConfirmButton(
                            text = "Deduct Stock",
                            containerColor = Palette.ButtonDarkBrown,
                            enabled = data.value.isNotEmpty(),
                            onClick = { onConfirm() }
                        )
                    }
                }
            }
        }
    }
}