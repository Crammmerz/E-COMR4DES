package com.android.inventorytracker.presentation.popup.item_insertion

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.presentation.popup.item_insertion.component.HeaderSection
import com.android.inventorytracker.presentation.shared.component.input_fields.DescriptionField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost

@Composable
fun InsertItemPopup(
    onDismiss: () -> Unit,
    onInsert: (ItemEntity) -> Unit
) {
    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var unitThreshold by rememberSaveable { mutableIntStateOf(1) }
    var subUnitThreshold by rememberSaveable { mutableIntStateOf(1) }
    var expiryThreshold by rememberSaveable { mutableIntStateOf(1) }
    var description by rememberSaveable { mutableStateOf("") }

    var nameValid by rememberSaveable { mutableStateOf(false) }
    var unitThresholdValid by rememberSaveable { mutableStateOf(true) }
    var expiryThresholdValid by rememberSaveable { mutableStateOf(true) }
    var subUnitThresholdValid by rememberSaveable { mutableStateOf(true) }

    val allValid = nameValid && unitThresholdValid && expiryThresholdValid && subUnitThresholdValid

    val focusName = remember { FocusRequester() }
    val focusUnit = remember { FocusRequester() }
    val focusSubUnit = remember { FocusRequester() }
    val focusExpiry = remember { FocusRequester() }
    val focusDescription = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusName.requestFocus()
    }

    DialogHost(
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .fillMaxHeight(0.8f),
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            HeaderSection()

            StringField(
                value = name,
                onValueChange = { name = it },
                header = "Item Name",
                placeholder = "Enter item name",
                modifier = Modifier.focusRequester(focusName),
                onValidationChange = { valid -> nameValid = valid },
                onDone = { focusUnit.requestFocus() }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                IntField(
                    modifier = Modifier.weight(1f),
                    value = unitThreshold,
                    onValueChange = { unitThreshold = it },
                    label = "Stock Threshold",
                    placeholder = "Enter unit threshold",
                    fieldModifier = Modifier.focusRequester(focusUnit),
                    onValidityChange = { unitThresholdValid = it },
                    onDone = { focusExpiry.requestFocus() },
                    doClear = true,
                )

                IntField(
                    modifier = Modifier.weight(1f),
                    value = expiryThreshold,
                    onValueChange = { expiryThreshold = it },
                    label = "Expiry Threshold",
                    placeholder = "Enter expiry threshold",
                    fieldModifier = Modifier.focusRequester(focusExpiry),
                    onValidityChange = { expiryThresholdValid = it },
                    onDone = { focusSubUnit.requestFocus() },
                    doClear = true,
                )
            }

            IntField(
                value = subUnitThreshold,
                onValueChange = { subUnitThreshold = it },
                label = "Sub Unit Threshold",
                placeholder = "Enter sub unit threshold",
                fieldModifier = Modifier.focusRequester(focusSubUnit),
                onDone = { focusDescription.requestFocus() },
                onValidityChange = { subUnitThresholdValid = it },
                doClear = true,
            )

            DescriptionField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxHeight(0.5f),
                fieldModifier = Modifier.focusRequester(focusDescription)
            )
        }

        Row(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CancelButton(onClick = onDismiss)

            ConfirmButton("Add Item", onClick = {
                if (allValid) {
                    val item = ItemEntity(
                        imageUri = null,
                        name = name.trim(),
                        unitThreshold = unitThreshold,
                        subUnitThreshold = subUnitThreshold,
                        expiryThreshold = expiryThreshold,
                        description = description.trim()
                    )
                    onInsert(item)
                    onDismiss()
                    Toast.makeText(context, "Item added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}



@Preview(
    showBackground = true,
    name = "AddNewItemPopup Preview",
    device = "spec:width=960dp,height=600dp,dpi=240,isRound=false,orientation=landscape"
)
@Composable
fun AddNewItemPopupPreview() {
    // Simple no-op callbacks for preview. onAdd receives the created ItemEntity.
    InsertItemPopup(
        onDismiss = { /* no-op for preview */ },
        onInsert = { item ->
            // no-op: in real usage you'd call viewModel.insertItem(item)
        }
    )
}