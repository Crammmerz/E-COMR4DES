package com.android.inventorytracking.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(bgColor: Color, modifier: Modifier = Modifier) {
    Surface(
        color = bgColor,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.075f) // fixed height for navbar
    ) {
        RightRow {
            Text(
                text = "Inventory Management 📦",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(10.dp))
            CenterButton(
                label = "🔔",
                bgColor = Color.Transparent,
                contentColor = Color.White,
                modifier = Modifier
                    .size(40.dp)
            ) {
                // Notification logic
            }

            CenterButton(
                label = "⚙️",
                bgColor = Color.Transparent,
                contentColor = Color.White,
                modifier = Modifier
                    .size(40.dp)
            ) {
                // Settings logic
            }
            Spacer(modifier = Modifier.width(30.dp))
        }
    }
}
