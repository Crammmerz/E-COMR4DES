package com.android.inventorytracker.presentation.popup.batch_group_removal

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.RemoveBatch
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.batch_group_removal.component.ItemRemovalRow
import com.android.inventorytracker.presentation.shared.component.SortDropdownMenu
import com.android.inventorytracker.presentation.shared.component.input_fields.SearchField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans

@Composable
fun BatchGroupRemovalPopup(
    model: List<ItemModel>,
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val persistentItems by itemViewModel.persistentItems.collectAsState()
    var inputMap by remember { mutableStateOf<Map<Int, RemoveBatch>>(emptyMap()) }
    var validityMap by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }
    var valid by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    LaunchedEffect(inputMap, validityMap) {
        val validItems = inputMap.filter { (id, _) -> id in persistentItems }.values.toList()
        valid = validItems.isNotEmpty() && validItems.all { validityMap[it.itemId] == true }
    }

    fun onConfirm(){
        if(valid){
            val validItems = inputMap.filter { (id, _) -> id in persistentItems && validityMap[id] == true }.values.toList()
            validItems.forEach { op ->
                batchViewModel.onDeductStock(op.batches, op.subunit)
            }
            itemViewModel.reset()
            onDismiss()
        }
    }

    Dialog(onDismissRequest = {
        itemViewModel.reset()
        onDismiss()
    }) {
        Card(
            modifier = Modifier
                .width(480.dp)
                .height(540.dp), // FIXED & UNIFORM HEIGHT
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                Text(
                    text = "Quick Remove Stock",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Palette.ButtonDarkBrown
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.focusable(false), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SearchField(Modifier.weight(1f))
                    SortDropdownMenu()
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(model.filter { it.totalSubUnit() != 0 }, key = { it.item.id }) { itemModel ->
                        val persistence = itemModel.item.id in persistentItems
                        ItemRemovalRow(
                            model = itemModel,
                            isPersistent = persistence,
                            onValueChange = { op -> if (persistence) inputMap = inputMap + (itemModel.item.id to op) },
                            onValidityChange = { isValid -> if (persistence) validityMap = validityMap + (itemModel.item.id to isValid) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "${persistentItems.size} items ready",
                        style = TextStyle(fontFamily = GoogleSans, fontSize = 12.sp, color = Color.Gray)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    CancelButton(onClick = {
                        itemViewModel.reset()
                        onDismiss()
                    })

                    ConfirmButton(
                        text = "Remove Stock",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = valid,
                        onClick = { showConfirmation = true }
                    )
                }
            }
        }
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Confirm Removal", style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Bold)) },
            text = { Text("Are you sure you want to deduct these items from stock? (FEFO basis)", style = TextStyle(fontFamily = GoogleSans)) },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text("Deduct", style = TextStyle(fontFamily = GoogleSans, color = Color.Red, fontWeight = FontWeight.Bold))
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmation = false }) {
                    Text("Cancel", style = TextStyle(fontFamily = GoogleSans, color = Color.Gray))
                }
            }
        )
    }
}