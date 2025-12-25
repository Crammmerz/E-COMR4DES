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
    var subUnitThresholdValid by rememberSaveable { mutableStateOf(true) }
    var expiryThresholdValid by rememberSaveable { mutableStateOf(true) }

    val allValid = nameValid && unitThresholdValid && subUnitThresholdValid && expiryThresholdValid

    val focusName = remember { FocusRequester() }
    val focusUnit = remember { FocusRequester() }
    val focusSubUnit = remember { FocusRequester() }
    val focusExpiry = remember { FocusRequester() }

    var annotation by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        focusName.requestFocus()
    }

    LaunchedEffect(expiryThreshold) {
        annotation = if (expiryThresholdValid) convertDaysToString(expiryThreshold) else ""
    }

    fun doInsert(){
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

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(480.dp) // Uniformed with other popups
                .height(540.dp), // FIXED HEIGHT
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                HeaderSection()

                Spacer(modifier = Modifier.height(16.dp))

                // Internal Scrollable Section
                Column(
                    modifier = Modifier
                        .weight(1f)
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
                        onValidationChange = { nameValid = it },
                        onDone = { focusUnit.requestFocus() }
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IntField(
                            modifier = Modifier.weight(1f),
                            fieldModifier = Modifier.focusRequester(focusUnit),
                            label = "Low Stock",
                            placeholder = "1",
                            doClear = true,
                            value = unitThreshold,
                            onValueChange = { unitThreshold = it },
                            onValidityChange = { unitThresholdValid = it },
                            onDone = { focusExpiry.requestFocus() }
                        )

                        IntField(
                            modifier = Modifier.weight(1f),
                            fieldModifier = Modifier.focusRequester(focusExpiry),
                            label = "Expiry Warning",
                            placeholder = "0",
                            doClear = true,
                            value = expiryThreshold,
                            onValueChange = { expiryThreshold = it },
                            annotation = annotation,
                            onValidityChange = { expiryThresholdValid = it },
                            onDone = { focusSubUnit.requestFocus() }
                        )
                    }

                    IntField(
                        modifier = Modifier.fillMaxWidth(),
                        fieldModifier = Modifier.focusRequester(focusSubUnit),
                        label = "Sub Unit Multiplier",
                        placeholder = "1",
                        doClear = true,
                        value = subUnitThreshold,
                        onValueChange = { subUnitThreshold = it },
                        onValidityChange = { subUnitThresholdValid = it }
                    )

                    DescriptionField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
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
                        text = if (allValid) "Ready to add" else "Required fields missing",
                        style = TextStyle(fontFamily = GoogleSans, fontSize = 12.sp, color = Color.Gray)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    CancelButton(onClick = onDismiss)

                    ConfirmButton(
                        text = "Add Item",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = allValid,
                        onClick = { doInsert() }
                    )
                }
            }
        }
    }
}