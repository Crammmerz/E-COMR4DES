package com.android.inventorytracker.presentation.home


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.home.component.ExpiryLevels
import com.android.inventorytracker.presentation.home.component.Header
import com.android.inventorytracker.presentation.home.component.StockLevels
import com.android.inventorytracker.presentation.home.component.QuickActions
import com.android.inventorytracker.util.toLocalDate

@Composable
fun Home(modifier: Modifier = Modifier, itemModels: List<ItemModel>) {
    val expiryItems = itemModels.filter { it.isExpiringSoon }.sortedBy { it.nearestExpiryDate }
    val stockItems = itemModels.filter { it.isLowStock }.sortedBy { it.totalUnit }

    Column(modifier = modifier.fillMaxSize().padding(20.dp)) {
        Header()
        QuickActions()
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(10.dp)){
            if(expiryItems.isNotEmpty()){
                ExpiryLevels (modifier = Modifier.weight(1f), itemModel = expiryItems)
            }
            if(stockItems.isNotEmpty()){
                StockLevels(modifier = Modifier.weight(1f), itemModel = stockItems)
            }
        }
    }
}
