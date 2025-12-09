package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
fun HeaderSection(itemModel: ItemModel){
    val totalUnit = itemModel.totalUnit()
    val threshold = itemModel.item.unitThreshold
    val darkRed = Color(0xFF8B0000)

    val stockColor = when {//TODO: Adjust Colors
        totalUnit == 0.0 -> Color.DarkGray
        totalUnit <= threshold * 0.2 -> darkRed
        else -> Color.White
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        // TODO: Image or Icon Display
        Text(
            text = "Item Details",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Column (
            modifier = Modifier
                .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                .background(stockColor, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Current Stocks",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp
            )
            Text(
                text = "${itemModel.totalUnitFormatted()} units",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        }
    }
}