package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.model.ItemModel

@Composable
fun StockLevels(modifier: Modifier, itemModel: List<ItemModel>){
    Column(modifier = modifier.background(Color.White).padding(50.dp)){
        Text(text = "Stocks", style = MaterialTheme.typography.titleLarge)
        LazyColumn (modifier = Modifier.padding(vertical = 10.dp).weight(1f)) {
            items(itemModel) { model ->
                Column {
                    Text(model.item.name)
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = { (model.totalUnit / model.item.unitThreshold).toFloat() },
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${model.totalUnitFormatted} / ${model.item.unitThreshold}",
                            modifier = Modifier.width(100.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}