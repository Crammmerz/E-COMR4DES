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
fun HeaderSection(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp + 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        // Alignment Spacers to match Image Width (48dp)
        Spacer(modifier = Modifier.width(48.dp))

        // Increased Item Name weight from 2.0f to 3.0f
        HeaderText("Item Name", Modifier.weight(3.0f))
        HeaderText("Expiry", Modifier.weight(1.2f))
        HeaderText("Stock", Modifier.weight(0.8f), TextAlign.Center)
        HeaderText("Current", Modifier.weight(0.8f), TextAlign.Center)

        // Actions column remains the same width (1.5f)
        HeaderText("Actions", Modifier.weight(1.5f), TextAlign.Center)
    }
}

@Composable
fun HeaderText(text: String, modifier: Modifier, align: TextAlign = TextAlign.Start) {
    Text(
        text = text,
        color = Color(0xFF8D6E63),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier,
        textAlign = align
    )
}