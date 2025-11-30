package com.android.inventorytracker.presentation.main

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
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@Composable
fun Main(
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val showNavBar by mainViewModel.showNavBar.collectAsState()
    Scaffold(topBar = { TopBar(mainViewModel) }) { inner ->
        Row(modifier = Modifier
            .padding(inner)
            .fillMaxSize()
        ) {
            if (showNavBar){
                NavBar(
                    modifier = Modifier.widthIn(min = 150.dp, max = 175.dp),
                    viewModel = mainViewModel
                )
            }
            ContentSection(
                modifier = Modifier.weight(1f),
                itemViewModel = itemViewModel,
                batchViewModel = batchViewModel,
                mainViewModel = mainViewModel
            )
        }
    }
}
