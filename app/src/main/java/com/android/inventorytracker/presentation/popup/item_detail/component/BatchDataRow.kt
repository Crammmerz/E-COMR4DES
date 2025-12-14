package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.util.toFormattedDateString

@Composable
fun BatchDataRow(threshold: Int, batch: ItemBatchEntity) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(batch.expiryDate.toFormattedDateString(), Modifier.weight(0.5f))
        Text("${batch.subUnit / threshold}", Modifier.weight(0.25f), textAlign = TextAlign.Center)
        Text("${batch.subUnit}", Modifier.weight(0.25f), textAlign = TextAlign.Center)
    }
}
