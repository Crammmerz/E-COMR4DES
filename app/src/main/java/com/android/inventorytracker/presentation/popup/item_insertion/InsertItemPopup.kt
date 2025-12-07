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
    var unitThresholdValid by rememberSaveable { mutableStateOf(false) }
    var expiryThresholdValid by rememberSaveable { mutableStateOf(false) }
    var subUnitThresholdValid by rememberSaveable { mutableStateOf(false) }

    val allValid = nameValid && unitThresholdValid && expiryThresholdValid && subUnitThresholdValid

    val focusName = remember { FocusRequester() }
    val focusUnit = remember { FocusRequester() }
    val focusSubUnit = remember { FocusRequester() }
    val focusExpiry = remember { FocusRequester() }
    val focusDescription = remember { FocusRequester() }

    focusName.requestFocus()

    DialogHost( // TODO: DialogHost for App testing while DialogMockup for Preview Testing
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
                fieldModifier = Modifier.focusRequester(focusName),
                isValid = { valid -> nameValid = valid },
                onDone = { focusUnit.requestFocus() }
            )
            Row (horizontalArrangement = Arrangement.spacedBy(10.dp)){
                IntField(
                    value = unitThreshold,
                    onValueChange = { unitThreshold = it },
                    header = "Stock Threshold",
                    placeholder = "Enter unit threshold",

                    modifier = Modifier.weight(1f),
                    fieldModifier = Modifier.focusRequester(focusUnit),
                    doClear = true,
                    isValid = { valid -> unitThresholdValid = valid },
                    onDone = { focusSubUnit.requestFocus() }
                )

                IntField(
                    value = expiryThreshold,
                    onValueChange = { expiryThreshold = it },
                    header = "Expiry Threshold",
                    placeholder = "Enter expiry threshold",
                    modifier = Modifier.weight(1f),
                    fieldModifier = Modifier.focusRequester(focusExpiry),
                    doClear = true,
                    isValid = { valid -> expiryThresholdValid = valid },
                    onDone = { focusDescription.requestFocus() }
                )
            }

            IntField(
                value = subUnitThreshold,
                onValueChange = { subUnitThreshold = it },
                header = "Sub Unit Threshold",
                placeholder = "Enter sub unit threshold",
                fieldModifier = Modifier.focusRequester(focusSubUnit),
                doClear = true,
                isValid = { valid -> subUnitThresholdValid = valid },
                onDone = { focusExpiry.requestFocus() }
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
                        Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            )
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