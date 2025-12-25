package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.ui.theme.GoogleSans

private val AccentColor = Color(0xFF5D4037)

@Composable
fun AddNewItemButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.shadow(4.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentColor,
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Add New Item",
            style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun DeleteItemButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    val dangerRed = Color(0xFF5D4037)

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, dangerRed),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = dangerRed,
            containerColor = Color.White
        )
    ) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "Remove",
            tint = dangerRed,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "Remove Items",
            style = TextStyle(
                fontFamily = GoogleSans,
                color = dangerRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}