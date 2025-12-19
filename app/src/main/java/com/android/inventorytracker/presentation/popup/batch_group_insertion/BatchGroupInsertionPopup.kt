package com.android.inventorytracker.presentation.popup.batch_group_insertion

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.android.inventorytracker.data.model.InsertBatch
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.batch_group_insertion.component.ItemInsertionRow
import com.android.inventorytracker.presentation.shared.component.SortDropdownMenu
import com.android.inventorytracker.presentation.shared.component.input_fields.SearchField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans // ✅ Imported

@Composable
fun BatchGroupInsertionPopup(
    model: List<ItemModel>,
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val persistentItems by itemViewModel.persistentItems.collectAsState()
    var inputMap by remember { mutableStateOf<Map<Int, InsertBatch>>(emptyMap()) }
    var validityMap by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }
    var showConfirmation by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = {
        itemViewModel.reset()
        onDismiss()
    }) {
        Card(
            modifier = Modifier.width(480.dp).height(620.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                Text(
                    text = "Quick Add",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Palette.ButtonDarkBrown // ✅ Fixed: 'TextDark' is replaced with an existing palette color
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SearchField(Modifier.weight(1f))
                    SortDropdownMenu()
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Select Items and Set Values",
                    style = TextStyle(fontFamily = GoogleSans, fontSize = 12.sp, color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(model, key = { it.item.id }) { itemModel ->
                        val persistence = itemModel.item.id in persistentItems
                        ItemInsertionRow(
                            model = itemModel,
                            isPersistent = persistence,
                            onValueChange = { op -> if (persistence) inputMap = inputMap + (itemModel.item.id to op) },
                            onValidityChange = { isValid -> if (persistence) validityMap = validityMap + (itemModel.item.id to isValid) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
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

                    CancelButton(text = "Close", onClick = {
                        itemViewModel.reset()
                        onDismiss()
                    })

                    ConfirmButton(
                        text = "Store Batch",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = persistentItems.isNotEmpty(),
                        onClick = { showConfirmation = true }
                    )
                }
            }
        }
    }

    if (showConfirmation) {
        val validItems = inputMap.filter { (id, _) -> id in persistentItems && validityMap[id] == true }.values.toList()
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Confirm Action", style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Bold)) },
            text = { Text("Do you want to add these batches?", style = TextStyle(fontFamily = GoogleSans)) },
            confirmButton = {
                TextButton(onClick = {
                    validItems.forEach { op ->
                        batchViewModel.onStoreBatch(ItemBatchEntity(itemId = op.itemId, subUnit = op.subunit, expiryDate = op.expiryDate))
                    }
                    itemViewModel.reset()
                    onDismiss()
                }) { Text("Confirm", style = TextStyle(fontFamily = GoogleSans, color = Palette.ButtonDarkBrown)) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmation = false }) { Text("Cancel", style = TextStyle(fontFamily = GoogleSans)) }
            }
        )
    }
}