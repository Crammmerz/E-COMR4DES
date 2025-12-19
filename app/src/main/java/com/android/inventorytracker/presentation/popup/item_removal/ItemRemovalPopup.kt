package com.android.inventorytracker.presentation.popup.item_removal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.item_removal.component.HeaderSection
import com.android.inventorytracker.presentation.popup.item_removal.component.InventoryItem
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun DeleteItemPopup(
    model: List<ItemModel>,
    onDelete: (ItemEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedIds = remember { mutableStateListOf<Int>() }
    var showConfirm by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(360.dp)
                .height(560.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Palette.PopupSurface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // HEADER (same hierarchy as InsertItemPopup)
                HeaderSection(onClose = onDismiss)

                // LIST
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(model, key = { it.item.id }) { itemModel ->
                        val id = itemModel.item.id
                        InventoryItem(
                            itemModel = itemModel,
                            isChecked = selectedIds.contains(id),
                            onCheckedChange = { checked ->
                                if (checked) selectedIds.add(id)
                                else selectedIds.remove(id)
                            }
                        )
                    }
                }

                Divider()

                // FOOTER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "${selectedIds.size} selected",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Cancel (reddish like Deduct)
                    CancelButton(
                        text = "Cancel",
                        onClick = onDismiss
                    )

                    ConfirmButton(
                        text = "Remove",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = selectedIds.isNotEmpty(),
                        onClick = { showConfirm = true }
                    )
                }
            }
        }
    }

    // CONFIRM DIALOG
    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Confirm Removal") },
            text = { Text("Remove ${selectedIds.size} selected item(s)?") },
            confirmButton = {
                TextButton(onClick = {
                    model.forEach {
                        if (selectedIds.contains(it.item.id)) {
                            onDelete(it.item)
                        }
                    }
                    showConfirm = false
                    onDismiss()
                }) {
                    Text("Remove", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
