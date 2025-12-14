package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderSection() {
    val headerColor = Color(0xFF463D35)

    Row(
        modifier = Modifier.padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = "Image",
            color = headerColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(64.dp)
        )

        Text(
            text = "Item Name",
            color = headerColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.weight(0.75f)
        )

        Text(
            text = "Expiry",
            color = headerColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.weight(0.5f)
        )

        Text(
            text = "Unit",
            color = headerColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.weight(0.5f)
        )

        Spacer(Modifier.weight(1f))
    }
}
