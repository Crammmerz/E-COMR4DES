package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    Row(Modifier.padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),) {
        Text(
            text = "Image",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(64.dp)
        )
        Text(
            text = "Item Name",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier
                .weight(0.75f)
                .padding(horizontal = 5.dp)
        )
        Text(
            text = "Expiry",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier
                .weight(0.5f)
                .padding(horizontal = 5.dp)
        )
        Text(
            text = "Unit",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier
                .weight(0.5f)
                .padding(horizontal = 5.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}