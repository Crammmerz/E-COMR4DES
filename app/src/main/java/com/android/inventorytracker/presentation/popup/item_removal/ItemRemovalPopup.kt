package com.android.inventorytracker.presentation.popup.item_removal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.item_removal.component.HeaderSection
import com.android.inventorytracker.presentation.popup.item_removal.component.InventoryItem
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost

@Composable
fun DeleteItemPopup(
    model: List<ItemModel>,
    onDelete: (ItemEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedIds = remember { mutableStateListOf<Int>() }
    var showDialog by remember { mutableStateOf(false) }

    if (!showDialog) {
        DialogHost(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .fillMaxHeight(0.6f),
            onDismissRequest = onDismiss,
            useImePadding = true
        ) {
            Column {
                HeaderSection()
                LazyColumn (Modifier.weight(1f)) {
                    items(model, key = { it.item.id }) { itemModel ->
                        val itemId = itemModel.item.id
                        val isSelected = selectedIds.contains(itemId)

                        InventoryItem(
                            itemModel = itemModel,
                            isChecked = isSelected,
                            onCheckedChange = { checked ->
                                if (checked) selectedIds.add(itemId)
                                else selectedIds.remove(itemId)
                            }
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CancelButton(onClick = onDismiss,)
                    ConfirmButton("Delete Item",onClick = { showDialog = true })
                }
            }
        }
    }

    // Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete ${selectedIds.size} item(s)?") },
            confirmButton = {
                TextButton(onClick = {
                    model.forEach { itemModel ->
                        if (selectedIds.contains(itemModel.item.id)) {
                            onDelete(itemModel.item)
                        }
                    }
                    selectedIds.clear()
                    showDialog = false
                    onDismiss()
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

