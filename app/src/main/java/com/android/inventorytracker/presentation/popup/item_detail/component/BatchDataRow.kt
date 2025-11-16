package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import java.text.DecimalFormat

@Composable
fun BatchDataRow(threshold: Int, batch: ItemBatchEntity){
    val df = DecimalFormat("#.####")
    val batchId = (batch.itemId*1000+batch.id).toString()
    val unit = df.format(batch.subUnit/threshold.toDouble())
    val subUnit = batch.subUnit.toString()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ){
        Text(batchId, modifier = Modifier.weight(0.2f))
        Text(batch.expiryDate, modifier = Modifier.weight(0.5f))
        //TODO: Expiry Level
        Text(unit, modifier = Modifier.weight(0.2f),
            textAlign = TextAlign.Center,)
        Text(subUnit,
            modifier = Modifier.weight(0.1f),
            textAlign = TextAlign.Center,)
    }
}