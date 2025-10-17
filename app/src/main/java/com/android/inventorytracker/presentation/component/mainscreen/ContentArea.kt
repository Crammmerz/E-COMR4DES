package com.android.inventorytracking.presentation.layout

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
import com.android.inventorytracker.presentation.viewmodel.Content
import com.android.inventorytracker.presentation.viewmodel.ContentViewModel
import com.android.inventorytracking.presentation.layouts.Home
import com.android.inventorytracking.presentation.layouts.Inventory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentArea(
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
