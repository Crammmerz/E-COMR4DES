package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Text(
            text = "Image",
            modifier = Modifier.weight(1f),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Item Name",
            modifier = Modifier.weight(3f),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Expiry",
            modifier = Modifier.weight(2f),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Unit",
            modifier = Modifier.weight(1f),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        // Actions column ( + / - / View More )
        Text(
            text = "",
            modifier = Modifier.weight(3f)
        )
    }
}
