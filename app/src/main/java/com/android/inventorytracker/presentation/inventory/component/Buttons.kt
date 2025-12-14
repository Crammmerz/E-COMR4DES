package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Button Colors
val DarkBrown = Color(0xFF4A3B32)
val AlertRed = Color(0xFFD32F2F)

@Composable
fun AddNewItemButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = DarkBrown),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text("Add Items", color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DeleteItemButton(onClick: () -> Unit, enabled: Boolean){
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = AlertRed,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.LightGray
        ),
        border = BorderStroke(1.dp, if(enabled) AlertRed else Color.LightGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Remove Items", fontWeight = FontWeight.Bold)
    }
}

// Deprecated in this new design, but kept if needed
@Composable
fun ItemButton(text: String, enabled: Boolean = true, modifier: Modifier, onClick: () -> Unit) {
    // ...
}