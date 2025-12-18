package com.android.inventorytracker.presentation.popup.item_removal.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// SAME color used in Add New Item header
private val TextDarkBrown = Color(0xFF4A3B32)

@Composable
fun HeaderSection(onClose: () -> Unit) {
    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Remove Inventory Items",
                color = TextDarkBrown,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = TextDarkBrown
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Select the items you want to remove from your inventory.",
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}
