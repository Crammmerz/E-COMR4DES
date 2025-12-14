package com.android.inventorytracker.presentation.popup.item_removal

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.item_removal.component.HeaderSection
import com.android.inventorytracker.presentation.popup.item_removal.component.InventoryItem
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost

private val CoffeeBrown = Color(0xFF5D4037)
private val PaperBackground = Color(0xFFFEF7ED)
private val DividerColor = Color(0xFFE0D6CC)
private val MutedText = Color(0xFF7A6A5F)

@Composable
fun DeleteItemPopup(
    model: List<ItemModel>,
    onDelete: (ItemEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedIds = remember { mutableStateListOf<Int>() }
    var showConfirm by remember { mutableStateOf(false) }

    DialogHost(
        modifier = Modifier
            .fillMaxWidth(0.35f)
            .fillMaxHeight(0.7f)
            .background(PaperBackground, RoundedCornerShape(16.dp)),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            HeaderSection(onClose = onDismiss)

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(model, key = { it.item.id }) { itemModel ->
                    val id = itemModel.item.id
                    InventoryItem(
                        itemModel = itemModel,
                        isChecked = selectedIds.contains(id),
                        onCheckedChange = {
                            if (it) selectedIds.add(id) else selectedIds.remove(id)
                        }
                    )
                }
            }

            Divider(color = DividerColor)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${selectedIds.size} items selected",
                    color = MutedText,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = CoffeeBrown)
                }

                Button(
                    onClick = { showConfirm = true },
                    enabled = selectedIds.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoffeeBrown,
                        disabledContainerColor = CoffeeBrown.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Remove Selected")
                }
            }
        }
    }

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
