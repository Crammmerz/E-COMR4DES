package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import java.text.DecimalFormat

@Composable
fun BatchDataRow(threshold: Int, batch: ItemBatchEntity){
    val df = DecimalFormat("#.####")
    val batchId = (batch.itemId*1000+batch.id).toString()
    val unit = df.format(batch.unit)
    val subUnit = ((batch.unit*threshold).toInt()).toString()
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ){
        Text(batchId, modifier = Modifier.weight(1f))
        Text(batch.expiryDate, modifier = Modifier.weight(1f))
        //TODO: Expiry Levels
        Text(unit, modifier = Modifier.weight(1f))
        Text(subUnit, modifier = Modifier.weight(1f))
    }
}