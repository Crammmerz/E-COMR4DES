package com.android.inventorytracker.presentation.popup.item_insertion

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.presentation.popup.item_insertion.component.HeaderSection
import com.android.inventorytracker.presentation.popup.item_insertion.component.PhotoSelectionButton
import com.android.inventorytracker.presentation.shared.component.input_fields.DescriptionField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.util.convertDaysToString

@Composable
fun InsertItemPopup(
    onDismiss: () -> Unit,
    onInsert: (ItemEntity) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var imageUri by rememberSaveable { mutableStateOf<String?>(null) }
    var name by rememberSaveable { mutableStateOf("") }
    var unitThreshold by rememberSaveable { mutableIntStateOf(1) }
    var subUnitThreshold by rememberSaveable { mutableIntStateOf(1) }
    var expiryThreshold by rememberSaveable { mutableIntStateOf(1) }
    var description by rememberSaveable { mutableStateOf("") }

    var nameValid by rememberSaveable { mutableStateOf(false) }
    var unitThresholdValid by rememberSaveable { mutableStateOf(true) }
    var expiryThresholdValid by rememberSaveable { mutableStateOf(true) }

    val allValid = nameValid && unitThresholdValid && expiryThresholdValid

    val focusName = remember { FocusRequester() }
    val focusUnit = remember { FocusRequester() }
    val focusExpiry = remember { FocusRequester() }

    var annotation by remember { mutableStateOf("") }

    LaunchedEffect(expiryThreshold) {
        annotation = if (expiryThresholdValid) convertDaysToString(expiryThreshold) else ""
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(480.dp)
                .heightIn(max = 620.dp), // Limitadong height para mag-trigger ang scroll
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize() // Siguraduhing gamit ang buong Card space
                    .padding(24.dp)
            ) {
                HeaderSection()

                Spacer(modifier = Modifier.height(16.dp))

                // Dito sa Column na ito dapat lilitaw lahat ng inputs
                Column(
                    modifier = Modifier
                        .weight(1f) // Pinaka-importante: Kumukuha ng available space
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PhotoSelectionButton(
                        modifier = Modifier.fillMaxWidth(),
                        image = imageUri,
                        onPickImage = { imageUri = it }
                    )

                    StringField(
                        value = name,
                        onValueChange = { name = it },
                        header = "Item Name",
                        placeholder = "Enter item name",
                        modifier = Modifier.focusRequester(focusName),
                        onValidationChange = { nameValid = it }
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IntField(
                            modifier = Modifier.weight(1f),
                            value = unitThreshold,
                            onValueChange = { unitThreshold = it },
                            label = "Stock Threshold",
                            placeholder = "1",
                            fieldModifier = Modifier.focusRequester(focusUnit),
                            onValidityChange = { unitThresholdValid = it },
                            doClear = true
                        )

                        IntField(
                            modifier = Modifier.weight(1f),
                            value = expiryThreshold,
                            onValueChange = { expiryThreshold = it },
                            label = "Expiry Threshold",
                            placeholder = "0",
                            annotation = annotation,
                            fieldModifier = Modifier.focusRequester(focusExpiry),
                            onValidityChange = { expiryThresholdValid = it },
                            doClear = true
                        )
                    }

                    // Eto yung DescriptionField, nilagyan ko ng height modifier para sure na may box
                    DescriptionField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp) // Explicit height para siguradong kita ang box
                    )
                }

                // Bottom Section (Buttons)
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (allValid) "Ready to add" else "Fill required fields",
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    CancelButton(onClick = onDismiss)

                    ConfirmButton(
                        text = "Add Item",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = allValid,
                        onClick = {
                            onInsert(
                                ItemEntity(
                                    imageUri = imageUri,
                                    name = name.trim(),
                                    unitThreshold = unitThreshold,
                                    subUnitThreshold = subUnitThreshold,
                                    expiryThreshold = expiryThreshold,
                                    description = description.trim()
                                )
                            )
                            onDismiss()
                            Toast.makeText(context, "Item added!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}