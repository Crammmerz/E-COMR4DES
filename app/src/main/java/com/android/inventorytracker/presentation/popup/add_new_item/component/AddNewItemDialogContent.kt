package com.android.inventorytracker.presentation.popup.add_new_item.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.presentation.shared.component.item_property_fields.DescriptionField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.NameField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.SubUnitField
import com.android.inventorytracker.presentation.shared.component.item_property_fields.ThresholdField

@Composable
fun AddNewItemDialogContent(
    name: String,
    onNameChange: (String) -> Unit,
    unitThreshold: Int,
    onUnitThresholdChange: (Int) -> Unit,
    subUnitThreshold: Int,
    onSubUnitThresholdChange: (Int) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onCancel: () -> Unit,
    onAdd: () -> Unit
) {
    Box {
        Column(Modifier.fillMaxSize().padding(5.dp)) {
            HeaderSection()

            NameField(
                name = name,
                onNameChange = onNameChange,
                modifier = Modifier.padding(top = 10.dp)
            )
            Row(Modifier.padding(top = 5.dp), horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                Column(Modifier.weight(1f)) {
                    ThresholdField(
                         unitThreshold = unitThreshold,
                         onThresholdChange =  onUnitThresholdChange,
                        Modifier.padding(top = 10.dp)
                    )
                }
                Column(Modifier.weight(1f)) {
                    SubUnitField(
                        subUnitThreshold = subUnitThreshold,
                        onSubUnitThresholdChange = onSubUnitThresholdChange,
                        Modifier.padding(vertical = 10.dp)
                    )
                }
            }

            DescriptionField(
                description = description,
                onDescriptionChange =  onDescriptionChange,
                Modifier.fillMaxHeight(0.5f)
            )
        }

        Row(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CancelButton(onClick = onCancel)
            AddItemButton(onClick = onAdd)
        }
    }
}