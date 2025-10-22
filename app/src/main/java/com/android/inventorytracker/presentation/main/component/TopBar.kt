package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.presentation.shared.component.primitive.RightRow

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    bgColor: Color = Color.White
) {
    Surface(
        color = bgColor,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.075f)
    ) {
        RightRow(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Inventory Tracking 📦",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}

