package com.android.inventorytracker.presentation.popup.item_detail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun ItemDetailPopup(
    model: ItemModel,
    loginViewModel: LoginViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onUpdateItem: (ItemEntity) -> Unit,
) {
    val role = loginViewModel.userRole
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Form States
    var imageUri by rememberSaveable(model.item.id) { mutableStateOf(model.item.imageUri) }
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

    fun onConfirm() {
        if (valid) {
            if (model.item.subUnitThreshold != subUnitThreshold) {
                batchViewModel.onConvertBatch(model.batch, model.item.subUnitThreshold, subUnitThreshold)
            }
            onUpdateItem(model.item.copy(name = name, imageUri = imageUri, description = description, unitThreshold = unitThreshold, expiryThreshold = expiryThreshold, subUnitThreshold = subUnitThreshold))
        }
    }

    LaunchedEffect(Unit) {
        if (model.item.subUnitThreshold > subUnitThreshold) {
            snapshotFlow { subUnitThreshold }
                .debounce(500) // wait 500ms after last change
                .distinctUntilChanged()
                .collectLatest { newValue ->
                    Toast.makeText(
                        context,
                        "Lowering this value reduces precision. Existing stock will be converted to larger units",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
        // 🔹 Ginaya ang Dialog properties para mawala ang "frame" sa labas ng card
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Pinaka-importante para sa custom size at rounded corners
        )
    ) {
        Card(
            modifier = Modifier
                .width(900.dp)
                .height(620.dp)
                .padding(16.dp), // Space sa labas ng card para makita ang shadow at corners
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                /* Header Section */
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Item Details",
                        style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Palette.DarkBeigeText)
                    )

                    Spacer(Modifier.weight(1f))

                    // Upper Right Label
                    Surface(
                        color = if (model.hasNoStock) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (model.hasNoStock) Color.Red else Color(0xFF4CAF50))
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, null, tint = if (model.hasNoStock) Color.Red else Color(0xFF4CAF50), modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(text = if (model.hasNoStock) "Out of Stock" else "In Stock", style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (model.hasNoStock) Color.Red else Color(0xFF2E7D32)))
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, null, tint = Palette.DarkBeigeText)
                    }
                }

                Spacer(Modifier.height(16.dp))

                /* Scrollable Body */
                Row(
                    modifier = Modifier.weight(1f).verticalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Left Side
                    Column(Modifier.weight(0.4f)) {
                        PhotoSelection(
                            modifier = Modifier.fillMaxWidth().aspectRatio(1.2f).clip(RoundedCornerShape(20.dp)),
                            image = imageUri,
                            enabled = role == UserRole.ADMIN,
                            onPickImage = { imageUri = it }
                        )
                        Spacer(Modifier.height(16.dp))
                        DescriptionField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier.fillMaxWidth().height(160.dp)
                        )
                    }

                    // Right Side
                    Column(Modifier.weight(0.6f)) {
                        Surface(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(6.dp)) {
                            Text(text = "ID: #${model.item.id}", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = TextStyle(fontFamily = GoogleSans, color = Color.Gray, fontSize = 12.sp))
                        }
                        Spacer(Modifier.height(12.dp))
                        StringField(value = name, header = "Item Name", placeholder = "Enter name", onValueChange = { name = it }, onValidityChange = { validName = it })
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            IntField(value = unitThreshold, label = "Stock Alert", placeholder = "0", modifier = Modifier.weight(1f), onValueChange = { unitThreshold = it }, onValidityChange = { validUnit = it })
                            IntField(value = expiryThreshold, label = "Expiry Alert", placeholder = "0", modifier = Modifier.weight(1f), onValueChange = { expiryThreshold = it }, onValidityChange = { validExpiry = it })
                        }
                        Spacer(Modifier.height(12.dp))
                        IntField(value = subUnitThreshold, label = "Sub Unit Partition", placeholder = "1", onValueChange = { subUnitThreshold = it }, onValidityChange = { validSubUnit = it })
                        Spacer(Modifier.height(20.dp))
                        BatchExpirySection(model = model, modifier = Modifier.heightIn(max = 300.dp))
                    }
                }

                /* Footer */
                Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    CancelButton(onClick = onDismiss)
                    ConfirmButton(
                        text = "Update Item",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = (name != model.item.name || unitThreshold != model.item.unitThreshold || expiryThreshold != model.item.expiryThreshold || subUnitThreshold != model.item.subUnitThreshold || imageUri != model.item.imageUri || description != model.item.description) && valid && role == UserRole.ADMIN,
                        onClick = {
                            if (unitThreshold != model.item.unitThreshold || subUnitThreshold != model.item.subUnitThreshold) showWarning = true
                            else onConfirm()
                        }
                    )
                }
            }
        }
    }

    // Warning AlertDialog (Remains standard)
    if (showWarning) {
        AlertDialog(
            onDismissRequest = { showWarning = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
            confirmButton = {
                Button(onClick = { onConfirm(); showWarning = false }, colors = ButtonDefaults.buttonColors(containerColor = Palette.ButtonDarkBrown)) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWarning = false }) { Text("Cancel", color = Color.Gray) }
            },
            title = { Text("Update Thresholds?", style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Bold)) },
            text = { Text("Proceed with changes to system calculations?", style = TextStyle(fontFamily = GoogleSans)) }
        )
    }
}