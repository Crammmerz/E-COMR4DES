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
import com.android.inventorytracker.Sand
import com.android.inventorytracker.presentation.home.Home
import com.android.inventorytracker.presentation.inventory.Inventory
import com.android.inventorytracker.presentation.main.viewmodel.Content
import com.android.inventorytracker.presentation.main.viewmodel.ContentViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentSection(
    modifier: Modifier = Modifier,
    bgColor: Color = Sand,
    contentViewModel: ContentViewModel = viewModel()
) {
    Surface(
        color = bgColor,
        tonalElevation = 10.dp,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        when(contentViewModel.currentContent){
            Content.Home -> Home()
            Content.Inventory -> Inventory()
        }
    }
}
