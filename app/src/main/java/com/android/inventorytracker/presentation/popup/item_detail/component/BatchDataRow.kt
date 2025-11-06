package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity

@Composable
fun BatchDataRow(threshold: Int, batch: ItemBatchEntity){
    val batchId = (batch.itemId*1000+batch.id).toString()
    val unit = (batch.subUnit/threshold).toString()
    val subUnit = batch.subUnit.toString()
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ){
        Text(batchId)
        Text(batch.expiryDate)
        //TODO: Expiry Levels
        Text(unit)
        Text(subUnit)
    }
}