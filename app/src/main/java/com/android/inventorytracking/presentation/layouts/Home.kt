package com.android.inventorytracking.presentation.layouts

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Home(bgColor: Color, modifier: Modifier = Modifier) {
    Surface(
        color = bgColor,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {

    }
}
