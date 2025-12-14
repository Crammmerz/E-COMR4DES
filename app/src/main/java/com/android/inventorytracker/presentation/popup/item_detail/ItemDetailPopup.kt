package com.android.inventorytracker.presentation.popup.item_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.popup.item_detail.component.*
import com.android.inventorytracker.presentation.shared.component.input_fields.*
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost

@Composable
fun ItemDetailPopup(
    itemModel: ItemModel,
    loginViewModel: LoginViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onUpdateItem: (ItemEntity) -> Unit,
    onUpdateBatch: (List<ItemBatchEntity>, Int, Int) -> Unit
) {
    val role = loginViewModel.userRole

    var imageUri by rememberSaveable { mutableStateOf(itemModel.item.imageUri) }
    var name by rememberSaveable { mutableStateOf(itemModel.item.name) }
    var unitThreshold by rememberSaveable { mutableIntStateOf(itemModel.item.unitThreshold) }
    var subUnitThreshold by rememberSaveable { mutableIntStateOf(itemModel.item.subUnitThreshold) }
    var expiryThreshold by rememberSaveable { mutableIntStateOf(itemModel.item.expiryThreshold) }
    var description by rememberSaveable { mutableStateOf(itemModel.item.description) }

    val updatedItem = itemModel.item.copy(
        imageUri = imageUri,
        name = name,
        unitThreshold = unitThreshold,
        subUnitThreshold = subUnitThreshold,
        expiryThreshold = expiryThreshold,
        description = description
    )

    var showAlert by remember { mutableStateOf(false) }
    val doUpdate = itemModel.item != updatedItem

    DialogHost(
        modifier = Modifier
            .width(850.dp)
            .height(520.dp)
            .background(Color(0xFFFEF7ED), RoundedCornerShape(14.dp)),
        onDismissRequest = onDismiss,
        useImePadding = true
    ) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {

            HeaderSection(itemModel)

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                /* LEFT CARD */
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    PhotoSelection(
                        image = imageUri,
                        role = role,
                        onPickImage = { if (role == UserRole.ADMIN) imageUri = it }
                    )

                    Spacer(Modifier.height(8.dp))
                    Text("ID: ${itemModel.item.id}", style = MaterialTheme.typography.labelSmall)

                    StringField(
                        value = name,
                        onValueChange = { if (role == UserRole.ADMIN) name = it },
                        header = "Item Name",
                        placeholder = "Enter item name",
                        onValidationChange = {},
                        onDone = {}
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IntField(
                            value = unitThreshold,
                            onValueChange = { if (role == UserRole.ADMIN) unitThreshold = it },
                            label = "Low Stock Threshold",
                            placeholder = "Threshold",
                            onValidityChange = {},
                            onDone = {}
                        )
                        IntField(
                            value = expiryThreshold,
                            onValueChange = { if (role == UserRole.ADMIN) expiryThreshold = it },
                            label = "Expiry Threshold",
                            placeholder = "Threshold",
                            onValidityChange = {},
                            onDone = {}
                        )
                    }

                    IntField(
                        value = subUnitThreshold,
                        onValueChange = { if (role == UserRole.ADMIN) subUnitThreshold = it },
                        label = "Sub Unit",
                        placeholder = "Enter value",
                        onValidityChange = {},
                        onDone = {}
                    )

                    if (itemModel.item.subUnitThreshold > subUnitThreshold) {
                        Text(
                            "⚠ Lowering this may convert existing stock.",
                            color = Color(0xFF8B0000),
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Button(
                        onClick = {
                            showAlert = true
                        },
                        enabled = doUpdate,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5D4037),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Update Item")
                    }
                }

                /* RIGHT CARD */
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    DescriptionField(
                        value = description,
                        onValueChange = { if (role == UserRole.ADMIN) description = it },
                        modifier = Modifier.weight(0.4f)
                    )

                    Spacer(Modifier.height(12.dp))

                    BatchExpirySection(
                        model = itemModel,
                        modifier = Modifier.weight(0.6f)
                    )
                }
            }
        }
    }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text("Confirm Update") },
            text = { Text("This change may affect existing stock. Proceed?") },
            confirmButton = {
                TextButton(onClick = {
                    onUpdateItem(updatedItem)
                    showAlert = false
                    onDismiss()
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showAlert = false }) { Text("Cancel") }
            }
        )
    }
}
