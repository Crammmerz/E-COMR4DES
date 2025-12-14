package com.android.inventorytracker.presentation.popup.item_removal.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CoffeeBrown = Color(0xFF5D4037)
private val MutedText = Color(0xFF7A6A5F)

@Composable
fun HeaderSection(onClose: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Remove Inventory Items",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = CoffeeBrown
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Text(
            text = "Select the items you want to remove from your inventory.",
            fontSize = 13.sp,
            color = MutedText
        )
    }
}
