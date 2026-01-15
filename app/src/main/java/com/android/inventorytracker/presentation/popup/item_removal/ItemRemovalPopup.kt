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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.item_removal.component.HeaderSection
import com.android.inventorytracker.presentation.popup.item_removal.component.InventoryItem
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans

@Composable
fun DeleteItemPopup(
    model: List<ItemModel>,
    onDelete: (List<Int>) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedIds = remember { mutableStateListOf<Int>() }
    var showConfirm by remember { mutableStateOf(false) }

    fun doDelete(){
        onDelete(selectedIds.toList())
        onDismiss()
        showConfirm = false
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(480.dp)
                .height(540.dp), // FIXED & UNIFORM HEIGHT
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                HeaderSection(onClose = onDismiss)

                Spacer(modifier = Modifier.height(16.dp))

                // Fixed internal scrolling
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(model, key = { it.item.id }) { itemModel ->
                        InventoryItem(
                            model = itemModel,
                            isChecked = selectedIds.contains(itemModel.item.id),
                            onCheckedChange = { checked ->
                                if (checked) selectedIds.add(itemModel.item.id)
                                else selectedIds.remove(itemModel.item.id)
                            }
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
                        text = "${selectedIds.size} items selected",
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    CancelButton(onClick = onDismiss)

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

    if(showConfirm){
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Remove Item?", style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Bold)) },
            text = {
                Text("Are you sure you want to remove the selected items? This will also remove all associated batches.",
                    style = TextStyle(fontFamily = GoogleSans))
            },
            confirmButton = {
                TextButton(onClick = { doDelete() }) {
                    Text("Remove", style = TextStyle(fontFamily = GoogleSans, color = Color.Red, fontWeight = FontWeight.Bold))
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Cancel", style = TextStyle(fontFamily = GoogleSans, color = Color.Gray))
                }
            }
        )
    }
}