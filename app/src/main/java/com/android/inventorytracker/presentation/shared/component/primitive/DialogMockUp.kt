package com.android.inventorytracker.presentation.shared.component.primitive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.ui.theme.LightSand

@Composable
fun DialogMockup( // used for testing buggy dialog preview
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .background(LightSand, shape = RoundedCornerShape(12.dp))
                .padding(16.dp),
            content = content
        )
    }
}
