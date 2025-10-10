package com.android.inventorytracking.presentation.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NavBar(bgColor: Color, modifier: Modifier = Modifier) {
    Surface(
        color = bgColor,
        tonalElevation = 10.dp,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.20f)// fixed width for sidebar
    ) {
        LeftColumn {
            Text(
                modifier = Modifier
                    .padding(
                        top = 15.dp,
                        bottom = 15.dp
                        ),
                text = "📦 Lumi Cafe",
                color = Color.White
            )

            LeftButton ("🏠 Home", Color.Transparent, Color.White) {

            }
            LeftButton("📦 Inventory", Color.Transparent, Color.White) {

            }
        }
    }
}



