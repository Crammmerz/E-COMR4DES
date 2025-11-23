package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.android.inventorytracker.presentation.home.Home
import com.android.inventorytracker.presentation.inventory.Inventory
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.presentation.main.viewmodel.Content
import com.android.inventorytracker.presentation.main.viewmodel.ContentViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.ui.theme.Sand

@Composable
fun ContentSection(
    modifier: Modifier = Modifier,
    bgColor: Color = Sand,
    itemViewModel: ItemViewModel,
    batchViewModel: BatchViewModel,
    contentViewModel: ContentViewModel
) {
    val itemModels by itemViewModel.itemModelList.collectAsState()
    val currentContent by contentViewModel.currentContent.collectAsState()

    Surface(
        color = bgColor,
        modifier = modifier.fillMaxSize()
    ) {
        when (currentContent) {
            Content.Home -> Home()
            Content.Inventory -> Inventory(itemModels = itemModels, itemViewModel = itemViewModel, batchViewModel = batchViewModel)
        }
    }
}

