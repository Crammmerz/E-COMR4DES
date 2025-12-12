package com.android.inventorytracker.presentation.home


import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
    val expiryItems by homeViewModel.expiryItems.collectAsState(initial = emptyList())
    val stockItems by homeViewModel.stockItems.collectAsState(initial = emptyList())

    Column(modifier = modifier
        .fillMaxSize()
        .padding(5.dp)) {
        Header()
        QuickActions()
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            if (expiryItems.isEmpty() && stockItems.isEmpty()) {
                // Show loading indicator while data is not ready
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ExpiryLevels(
                    modifier = Modifier.weight(1f),
                    itemModel = expiryItems
                )
                StockLevels(
                    modifier = Modifier.weight(1f),
                    itemModel = stockItems
                )
            }
        }
    }
}
