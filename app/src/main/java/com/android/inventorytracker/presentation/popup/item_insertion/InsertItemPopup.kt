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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.presentation.popup.item_insertion.component.HeaderSection
import com.android.inventorytracker.presentation.shared.component.item_property_fields.DescriptionField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.NameField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.SubUnitField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.UnitField
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
    var unitThreshold by rememberSaveable { mutableIntStateOf(0) }
    var subUnitThreshold by rememberSaveable { mutableIntStateOf(0) }
    var description by rememberSaveable { mutableStateOf("") }

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

            NameField(
                name = name,
                onNameChange = { name = it },
                modifier = Modifier.padding(top = 10.dp)
            )

            Row(
                modifier = Modifier.padding(top = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    UnitField(
                        unit = unitThreshold,
                        onUnitChange = { unitThreshold = it },
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
                Column(Modifier.weight(1f)) {
                    SubUnitField(
                        subUnit = subUnitThreshold,
                        onSubUnitChange = { subUnitThreshold = it },
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }

            DescriptionField(
                description = description,
                onDescriptionChange = { description = it },
                modifier = Modifier.fillMaxHeight(0.5f)
            )
        }

        Row(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CancelButton(onClick = onDismiss)

            ConfirmButton("Add Item",onClick = {
                    val trimmedName = name.trim()
                    val trimmedDescription = description.trim()

                    if (trimmedName.isNotEmpty() && unitThreshold > 0 && subUnitThreshold > 0) {
                        val item = ItemEntity(
                            imageUri = null,
                            name = trimmedName,
                            unitThreshold = unitThreshold,
                            subUnitThreshold = subUnitThreshold,
                            description = trimmedDescription
                        )
                        onInsert(item)
                        onDismiss()
                        Toast.makeText(context, "Item added!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
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