package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.ItemModel


@Composable
fun HeaderSection(itemModel: ItemModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            "Item Details",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.weight(1f))
        Column(
            modifier = Modifier
                .background(Color(0xFF5D4037), RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text("Current Stock", color = Color.White, fontSize = 10.sp)
            Text(
                "${itemModel.totalUnitFormatted()} units",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}
