package com.android.inventorytracker.presentation.popup.item_removal.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.inventorytracker.data.model.ItemModel

@Composable
fun InventoryItem(
    itemModel: ItemModel,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
        // TODO: Image or other UI
        Text(itemModel.item.name)
    }
}
