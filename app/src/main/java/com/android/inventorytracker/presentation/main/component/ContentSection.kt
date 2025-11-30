package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.home.Home
import com.android.inventorytracker.presentation.inventory.Inventory
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.presentation.main.viewmodel.Content
import com.android.inventorytracker.presentation.main.viewmodel.MainViewModel
import com.android.inventorytracker.presentation.setting.Setting
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.ui.theme.Sand

@Composable
fun ContentSection(
    modifier: Modifier = Modifier,
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    mainViewModel: MainViewModel,
) {
    val itemModels by itemViewModel.itemModelList.collectAsState()
    val currentContent by mainViewModel.currentContent.collectAsState()

    Surface(
        color = Sand,
        modifier = modifier.fillMaxSize()
    ) {
        when (currentContent) {
            Content.Home -> Home()
            Content.Inventory -> Inventory(itemModels = itemModels, itemViewModel = itemViewModel, batchViewModel = batchViewModel)
            Content.Setting -> Setting()
        }
    }
}