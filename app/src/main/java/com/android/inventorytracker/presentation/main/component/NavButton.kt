package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.android.inventorytracker.presentation.shared.component.primitive.LeftButton

@Composable
fun NavButton(
    label: String,
    bgColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    LeftButton(
        label = label,
        bgColor = bgColor,
        contentColor = Color.White,
        onClick = onClick,
        modifier = Modifier
            .fillMaxHeight(0.075f)
    )
}