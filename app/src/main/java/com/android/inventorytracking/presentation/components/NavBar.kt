package com.android.inventorytracking.presentation.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NavBar(bgColor: Color, modifier: Modifier = Modifier) {
    Surface(
        color = bgColor,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.075f) // fixed height for navbar
    ) {
        // Add NavBar content here
    }
}

@Preview(
    showBackground = true,
    name = "The Preview",
    device = "spec:width=900dp,height=550dp,dpi=420,isRound=false,orientation=landscape"
)
@Composable
fun MainPanelPreview() {
    MaterialTheme {
        NavBar(Color.White)
    }
}