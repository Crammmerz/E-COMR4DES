package com.android.inventorytracker.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.home.component.ExpiryLevels
import com.android.inventorytracker.presentation.home.component.Header
import com.android.inventorytracker.presentation.home.component.StockLevels
import com.android.inventorytracker.presentation.home.component.QuickActions
import com.android.inventorytracker.presentation.home.viewmodel.HomeViewModel

// Background similar to login screen
private val LumiBackgroundWhite = Color(0xFFFFFBF4)

@Composable
fun Home(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val itemModels by homeViewModel.itemModelList.collectAsState(initial = emptyList())
    val expiryItems = itemModels
        .filter { it.isExpiringSoon }
        .sortedBy { it.nearestExpiryDate }
    val stockItems = itemModels
        .filter { it.isLowStock }
        .sortedBy { it.totalUnit() / it.item.unitThreshold * 0.20f }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(LumiBackgroundWhite),
        color = LumiBackgroundWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Header()

            QuickActions()

            // Main content cards row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                if (expiryItems.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp,
                        color = Color.White
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            ExpiryLevels(
                                modifier = Modifier.fillMaxSize(),
                                itemModel = expiryItems
                            )
                        }
                    }
                }

                if (stockItems.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp,
                        color = Color.White
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            StockLevels(
                                modifier = Modifier.fillMaxSize(),
                                itemModel = stockItems
                            )
                        }
                    }
                }
            }
        }
    }
}
