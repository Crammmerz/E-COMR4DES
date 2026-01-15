package com.android.inventorytracker.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.main.component.ContentSection
import com.android.inventorytracker.presentation.main.component.NavBar
import com.android.inventorytracker.presentation.main.component.TopBar
import com.android.inventorytracker.presentation.main.viewmodel.MainViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.android.inventorytracker.presentation.home.viewmodel.HomeViewModel
import com.android.inventorytracker.presentation.main.component.Notification


@Composable
fun Main(
    mainViewModel: MainViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val showNavBar by mainViewModel.showNavBar.collectAsState()
    val showNotif by mainViewModel.showNotif.collectAsState()

    val expiryItems by homeViewModel.expiringItems.collectAsState(initial = emptyList())
    val stockItems by homeViewModel.stockItems.collectAsState(initial = emptyList())

    Scaffold(topBar = { TopBar(mainViewModel) }) { inner ->
        Box (Modifier.padding(inner)){
            Row(modifier = Modifier.fillMaxSize()){
                NavBar(
                    isOpen = showNavBar,
                    modifier = Modifier
                        .widthIn(min = 150.dp, max = 175.dp)
                        .fillMaxHeight(),
                    mainViewModel = mainViewModel
                )
                ContentSection(
                    modifier = Modifier.weight(1f),
                    mainViewModel = mainViewModel,
                )
            }
            if (showNotif) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.5f))
                        .fillMaxSize()
                        .clickable(onClick = { mainViewModel.setNotif(false) })
                ) {
                    Notification(
                        expiryItems = expiryItems,
                        stockModel = stockItems,
                        modifier = Modifier.align(Alignment.CenterEnd) // stick to right edge
                    )
                }
            }
        }
    }
}
