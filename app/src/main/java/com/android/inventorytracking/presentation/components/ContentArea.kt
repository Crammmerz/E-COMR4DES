package com.android.inventorytracking.presentation.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ContentArea(bgColor: Color, modifier: Modifier = Modifier) {
    Surface(
        color = bgColor,
        tonalElevation = 10.dp,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()// fixed width for sidebar
    ) {

    }
}
