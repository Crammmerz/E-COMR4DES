package com.android.inventorytracker.presentation.popup.add_new_item.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun HeaderSection(){
    Text(
        text = "Add New Item",
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    )
    Text(
        text = "Fill in the details to add a new item in your inventory.",
        color = Color.Black,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    )
}