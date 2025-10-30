package com.android.inventorytracker.presentation.popup.add_new_item

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.presentation.popup.add_new_item.component.AddNewItemDialogContent
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.component.primitive.DialogMockup

@Composable
fun AddNewItemPopup(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onAdd: (ItemEntity) -> Unit
) {
    if (!isVisible) return

    val context = LocalContext.current
    var name by rememberSaveable { mutableStateOf("") }
    var unitThreshold by rememberSaveable { mutableIntStateOf(1) }
    var subUnitThreshold by rememberSaveable { mutableIntStateOf(1) }
    var description by rememberSaveable { mutableStateOf("") }

    DialogHost (// TODO: DialogHost for App testing while DialogMockup for Preview Testing
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .fillMaxHeight(0.8f)
            .imePadding(),
        onDismissRequest = onDismiss
    ) {
        AddNewItemDialogContent(
            name = name,
            onNameChange = { name = it },
            unitThreshold = unitThreshold,
            onUnitThresholdChange = { unitThreshold = it },
            subUnitThreshold = subUnitThreshold,
            onSubUnitThresholdChange = { subUnitThreshold = it },
            description = description,
            onDescriptionChange = { description = it },
            onCancel = onDismiss,
            onAdd = {
                val trimmedName = name.trim()
                if (trimmedName.isNotEmpty() && unitThreshold > 0 && subUnitThreshold > 0) {
                    val item = ItemEntity(
                        imageUri = null,
                        name = trimmedName,
                        unitThreshold = unitThreshold,
                        subUnitThreshold = subUnitThreshold,
                        description = description.trim()
                    )
                    onAdd(item)
                    onDismiss()
                    Toast.makeText(context, "Item added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                }
            }
        )
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
    AddNewItemPopup(
        isVisible = true,
        onDismiss = { /* no-op for preview */ },
        onAdd = { item ->
            // no-op: in real usage you'd call viewModel.insertItem(item)
        }
    )
}