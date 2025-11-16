package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.shared.component.primitive.CenterColumn
import com.android.inventorytracker.presentation.shared.component.primitive.CenterRow
import com.android.inventorytracker.ui.theme.LightSand
import java.text.DecimalFormat


@Composable
fun HeaderSection(itemModel: ItemModel){
    val totalUnit = itemModel.totalUnit
    val threshold = itemModel.item.unitThreshold
    val darkRed = Color(0xFF8B0000)

    val stockColor = when {//TODO: Adjust Colors
        totalUnit == 0.0 -> Color.DarkGray
        totalUnit <= threshold * 0.2 -> darkRed
        else -> Color.White
    }

    CenterRow {
        // TODO: Image or Icon Display
        Text(
            text = "Item Details",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        CenterColumn (modifier = Modifier
            .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
            .background(stockColor, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = "Current Stocks",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
            Text(
                text = "${itemModel.totalUnitFormatted} units",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}