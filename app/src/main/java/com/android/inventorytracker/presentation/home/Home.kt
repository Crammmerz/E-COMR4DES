package com.android.inventorytracker.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.home.component.ExpiryLevels
import com.android.inventorytracker.presentation.home.component.Header
import com.android.inventorytracker.presentation.home.component.StockLevels
import com.android.inventorytracker.presentation.home.component.QuickActions
import com.android.inventorytracker.presentation.home.viewmodel.HomeViewModel
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun Home(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = hiltViewModel()) {
    val expiryItems by homeViewModel.expiryItems.collectAsState(initial = emptyList())
    val stockItems by homeViewModel.stockItems.collectAsState(initial = emptyList())

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(Palette.iOSBackgroundLight),
        color = Palette.iOSBackgroundLight
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Header()

            Box(modifier = Modifier.fillMaxWidth()) {
                QuickActions()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.Top
            ) {
                if (expiryItems.isEmpty() && stockItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(32.dp),
                        tonalElevation = 0.dp,
                        shadowElevation = 8.dp,
                        color = Palette.iOSCardWhite
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp)
                        ) {
                            ExpiryLevels(
                                modifier = Modifier.fillMaxSize(),
                                itemModel = expiryItems
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(32.dp),
                        tonalElevation = 0.dp,
                        shadowElevation = 8.dp,
                        color = Palette.iOSCardWhite
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp)
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