package com.android.inventorytracker.presentation.popup.item_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.popup.item_detail.component.*
import com.android.inventorytracker.presentation.shared.component.input_fields.*
import com.android.inventorytracker.presentation.shared.component.primitive.*
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun ItemDetailPopup(
    model: ItemModel,
    loginViewModel: LoginViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onUpdateItem: (ItemEntity) -> Unit,
) {
    val role = loginViewModel.userRole

    var imageUri by rememberSaveable(model.item.id) {
        mutableStateOf(model.item.imageUri)
    }
    var name by rememberSaveable(model.item.id) { mutableStateOf(model.item.name) }
    var unitThreshold by rememberSaveable(model.item.id) { mutableIntStateOf(model.item.unitThreshold) }
    var expiryThreshold by rememberSaveable(model.item.id) { mutableIntStateOf(model.item.expiryThreshold) }
    var subUnitThreshold by rememberSaveable(model.item.id) { mutableIntStateOf(model.item.subUnitThreshold) }
    var description by rememberSaveable(model.item.id) { mutableStateOf(model.item.description) }

    var validName by remember { mutableStateOf(true) }
    var validUnit by remember { mutableStateOf(true) }
    var validExpiry by remember { mutableStateOf(true) }
    var validSubUnit by remember { mutableStateOf(true) }
    var showWarning by remember { mutableStateOf(false) }

    val valid = validName && validUnit && validExpiry && validSubUnit

    fun shouldTriggerWarning(): Boolean{
        return unitThreshold != model.item.unitThreshold ||
                expiryThreshold != model.item.expiryThreshold ||
                subUnitThreshold != model.item.subUnitThreshold
    }
    fun hasChanged(): Boolean{
        return name != model.item.name ||
                imageUri != model.item.imageUri ||
                description != model.item.description ||
                unitThreshold != model.item.unitThreshold ||
                expiryThreshold != model.item.expiryThreshold ||
                subUnitThreshold != model.item.subUnitThreshold
    }

    fun onConfirm(){
        if(valid) {
            if(model.item.subUnitThreshold != subUnitThreshold){
                batchViewModel.onConvertBatch(
                    batches = model.batch,
                    subUnitThreshold = model.item.subUnitThreshold,
                    newSubUnitThreshold = subUnitThreshold
                )
            }
            onUpdateItem(
                model.item.copy(
                    name = name,
                    imageUri = imageUri,
                    description = description,
                    unitThreshold = unitThreshold,
                    expiryThreshold = expiryThreshold,
                    subUnitThreshold = subUnitThreshold
                )
            )
        }
    }

    DialogHost(
        modifier = Modifier
            .width(900.dp)
            .height(650.dp),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            /* ---------------- Header ---------------- */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Item Details",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = GoogleSans
                )

                Spacer(Modifier.weight(1f))

                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFF4CAF50))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "In Stock",
                            color = Color(0xFF2E7D32),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }

            Spacer(Modifier.height(24.dp))

            /* ---------------- Body ---------------- */
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.weight(1f)
            ) {

                /* -------- Left Column -------- */
                Column(Modifier.weight(0.4f)) {
                    PhotoSelection(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        image = imageUri,
                        enabled = role == UserRole.ADMIN,
                        onPickImage = { imageUri = it }
                    )

                    Spacer(Modifier.height(16.dp))

                    DescriptionField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }

                /* -------- Right Column -------- */
                Column(Modifier.weight(0.6f)) {

                    Surface(
                        color = Color(0xFFEEEEEE),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "ID: #${model.item.id}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    StringField(
                        value = name,
                        header = "Item Name",
                        placeholder = "Enter item name",
                        onValueChange = { name = it },
                        onValidationChange = { validName = it }
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        IntField(
                            value = unitThreshold,
                            label = "Low Stock",
                            placeholder = "0",
                            modifier = Modifier.weight(1f),
                            onValueChange = { unitThreshold = it },
                            onValidityChange = { validUnit = it }
                        )

                        IntField(
                            value = expiryThreshold,
                            label = "Expiry",
                            placeholder = "Days",
                            modifier = Modifier.weight(1f),
                            onValueChange = { expiryThreshold = it },
                            onValidityChange = { validExpiry = it }
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    IntField(
                        value = subUnitThreshold,
                        label = "Sub Unit",
                        placeholder = "0",
                        onValueChange = { subUnitThreshold = it },
                        onValidityChange = { validSubUnit = it }
                    )
                    if(model.item.subUnitThreshold > subUnitThreshold){
                        Text(
                            text = "⚠ Lowering this value reduces precision. Existing stock will be converted to larger units",
                            color = Color.Red,
                            fontSize = 10.sp
                        )
                    }
                    Spacer(Modifier.height(16.dp))

                    BatchExpirySection(
                        model = model,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            /* ---------------- Footer ---------------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CancelButton(onClick = onDismiss)

                ConfirmButton(
                    text = "Update Item",
                    containerColor = Palette.ButtonDarkBrown,
                    enabled = hasChanged() && valid,
                    onClick = {
                        if(shouldTriggerWarning()){
                            showWarning = true
                        } else {
                            onConfirm()
                        }
                    }
                )
            }
        }
    }

    if(showWarning){
        AlertDialog(
            onDismissRequest = { showWarning = false },
            title = { Text("Warning") },
            text = {
                Column {
                    Text("Heads up: modifying these values means the app will apply different logic going forward.")
                    Text("Proceed?")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                        onConfirm()
                        showWarning = false
                    }
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWarning = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
