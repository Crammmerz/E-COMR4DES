package com.android.inventorytracker.presentation.popup.item_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.popup.item_detail.component.BatchExpirySection
import com.android.inventorytracker.presentation.popup.item_detail.component.PhotoSelection
import com.android.inventorytracker.presentation.shared.component.input_fields.DescriptionField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.util.convertDaysToString

@Composable
fun ItemDetailPopup(
    itemModel: ItemModel,
    loginViewModel: LoginViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onUpdateItem: (ItemEntity) -> Unit,
    onUpdateBatch: (List<ItemBatchEntity>, Int, Int) -> Unit
) {
    val role = loginViewModel.userRole

    var imageUri by rememberSaveable(itemModel.item.id) { mutableStateOf(itemModel.item.imageUri) }
    var name by rememberSaveable(itemModel.item.id) { mutableStateOf(itemModel.item.name) }
    var unitThreshold by rememberSaveable(itemModel.item.id) { mutableIntStateOf(itemModel.item.unitThreshold) }
    var subUnitThreshold by rememberSaveable(itemModel.item.id) { mutableIntStateOf(itemModel.item.subUnitThreshold) }
    var expiryThreshold by rememberSaveable(itemModel.item.id) { mutableIntStateOf(itemModel.item.expiryThreshold) }
    var description by rememberSaveable(itemModel.item.id) { mutableStateOf(itemModel.item.description) }

    var annotation by remember{ mutableStateOf("") }

    val updatedItem = itemModel.item.copy(
                imageUri = imageUri,
                name = name,
                unitThreshold = unitThreshold,
                subUnitThreshold = subUnitThreshold,
                expiryThreshold = expiryThreshold,
                description = description
            )

    var nameValid by remember { mutableStateOf(true) }
    var isStockThresholdValid by remember { mutableStateOf(true) }
    var expiryThresholdValid by remember { mutableStateOf(true) }
    var subUnitThresholdValid by remember { mutableStateOf(true) }

    var showAlert by remember { mutableStateOf(false) }
    var riskyFieldChanged by rememberSaveable { mutableStateOf(false) }
    val doUpdate = itemModel.item != updatedItem
    val allValid = nameValid && isStockThresholdValid && expiryThresholdValid && subUnitThresholdValid

    LaunchedEffect(expiryThreshold) {
        annotation = if (expiryThresholdValid) {
            convertDaysToString(expiryThreshold)
        } else {
            ""
        }
    }
    fun checkRiskyChanges(): Boolean {
        return itemModel.item.unitThreshold != updatedItem.unitThreshold ||
                itemModel.item.subUnitThreshold != updatedItem.subUnitThreshold ||
                itemModel.item.expiryThreshold != updatedItem.expiryThreshold
    }

    fun onUpdateItem() {
        if (allValid && doUpdate) {
            if (itemModel.item.subUnitThreshold != updatedItem.subUnitThreshold) {
                onUpdateBatch(
                    itemModel.batch,
                    itemModel.item.subUnitThreshold,
                    updatedItem.subUnitThreshold
                )
            }
            onUpdateItem(updatedItem)
        }
    }

    DialogHost(
        modifier = Modifier
            .height(500.dp)
            .width(800.dp),
        useImePadding = true,
        onDismissRequest = { onDismiss() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row(
                Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(Modifier.weight(0.40f)) {
                    PhotoSelection(
                        image = imageUri,
                        enabled = role == UserRole.ADMIN,
                        onPickImage = { if(role == UserRole.ADMIN) imageUri = it }
                    )
                    Text("ID: ${itemModel.item.id}")
                    StringField(
                        value = name,
                        onValueChange = { if(role == UserRole.ADMIN) name = it },
                        header = "Item Name",
                        placeholder = "Enter item name",
                        onValidationChange = { nameValid = it },
                        onDone = {  }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(Modifier.weight(1f)) {
                            IntField(
                                value = unitThreshold,
                                onValueChange = { if(role == UserRole.ADMIN) unitThreshold = it },
                                label = "Low Stock Threshold",
                                placeholder = "Enter threshold",
                                onValidityChange = { isStockThresholdValid = it },
                                onDone = {  }
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            IntField(
                                value = expiryThreshold,
                                onValueChange = { if(role == UserRole.ADMIN) expiryThreshold = it },
                                label = "Expiry Threshold",
                                placeholder = "Enter threshold (Days)",
                                annotation = annotation,
                                onValidityChange = {  expiryThresholdValid = it },
                                onDone = {  }
                            )
                        }
                    }
                    IntField(
                        value = subUnitThreshold,
                        onValueChange = { if(role == UserRole.ADMIN) subUnitThreshold = it },
                        label = "Sub Unit",
                        placeholder = "Enter threshold",
                        onValidityChange = { subUnitThresholdValid = it },
                        onDone = { }
                    )
                    if(itemModel.item.subUnitThreshold > subUnitThreshold){
                        Text(
                            text = "⚠️ Lowering this value reduces precision. Existing stock will be converted to larger units.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Button(onClick = {
                        riskyFieldChanged = checkRiskyChanges()
                        if (riskyFieldChanged) {
                            showAlert = true
                        } else {
                            onUpdateItem()
                            onDismiss()
                        }
                    },
                        enabled = allValid && doUpdate
                    ) {
                        Text("Update Item")
                    }
                }

                Column(Modifier.weight(0.60f)) {
                    DescriptionField(
                        value = description,
                        onValueChange = { if(role == UserRole.ADMIN) description = it },
                        modifier = Modifier.weight(0.45f)
                    )

                    Spacer(Modifier.height(10.dp))

                    BatchExpirySection(
                        model = itemModel,
                        modifier = Modifier.weight(0.55f)
                    )
                }
            }
        }
    }
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text("Confirm Risky Update") },
            text = { Text("⚠️ Updating thresholds or expiry may affect stock alerts and batch logic. Proceed?") },
            confirmButton = {
                TextButton(onClick = {
                    onUpdateItem()
                    showAlert = false
                    onDismiss()
                }) { Text("Yes, Update") }
            },
            dismissButton = {
                TextButton(onClick = { showAlert = false }) { Text("Cancel") }
            }
        )
    }
}
