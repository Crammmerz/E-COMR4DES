package com.android.inventorytracker.presentation.home


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.home.component.ExpiryLevels
import com.android.inventorytracker.presentation.home.component.Header
import com.android.inventorytracker.presentation.home.component.StockLevels
import com.android.inventorytracker.presentation.home.component.QuickActions
import com.android.inventorytracker.presentation.home.viewmodel.HomeViewModel

@Composable
fun Home(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = hiltViewModel()) {
    val itemModels by homeViewModel.itemModelList.collectAsState(initial = emptyList())
    val expiryItems = itemModels.filter { it.isExpiringSoon }.sortedBy { it.nearestExpiryDate }
    val stockItems = itemModels.filter { it.isLowStock }.sortedBy { it.totalUnit()/it.item.unitThreshold*0.20f }

    Column(modifier = modifier.fillMaxSize().padding(5.dp)) {
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
