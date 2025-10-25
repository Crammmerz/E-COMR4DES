package com.android.inventorytracker.presentation.main.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.inventorytracker.presentation.home.Home
import com.android.inventorytracker.presentation.inventory.Inventory
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.presentation.main.viewmodel.Content
import com.android.inventorytracker.presentation.main.viewmodel.ContentViewModel
import com.android.inventorytracker.ui.theme.Sand

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentSection(
    modifier: Modifier = Modifier,
    bgColor: Color = Sand,
    contentViewModel: ContentViewModel = viewModel()
) {
    val itemViewModel: ItemViewModel = viewModel()
    Surface(
        color = bgColor,
        tonalElevation = 10.dp,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        when(contentViewModel.currentContent){
            Content.Home -> Home()
            Content.Inventory -> Inventory(itemViewModel)
        }
    }
}
