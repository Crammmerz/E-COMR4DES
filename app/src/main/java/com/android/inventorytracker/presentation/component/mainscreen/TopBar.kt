package com.android.inventorytracking.presentation.layout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.inventorytracking.presentation.elements.RightRow

@Composable
fun TopBar(modifier: Modifier = Modifier, bgColor: Color = Color.White ) {
    Surface(
        color = bgColor,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.075f) // fixed height for navbar
    ) {
        RightRow(modifier = Modifier.padding(5.dp)) {
            TopBarElements()
        }
    }
}

@Composable
fun TopBarElements(){
    Text(
        text = "Inventory Tracking 📦",
        color = Color.Black,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.width(10.dp))
}

