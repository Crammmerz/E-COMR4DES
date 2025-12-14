package com.android.inventorytracker.presentation.popup.item_insertion.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val TextDarkBrown = Color(0xFF4A3B32)

@Composable
fun HeaderSection(){
    Column {
        Text(
            text = "Add New Item",
            color = TextDarkBrown, // Theme color
            fontWeight = FontWeight.ExtraBold, // Increased boldness
            fontSize = 24.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Fill in the details to add a new item in your inventory.",
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(8.dp))
    }
}