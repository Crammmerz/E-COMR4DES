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
import com.android.inventorytracker.presentation.shared.component.primitive.CenterColumn
import com.android.inventorytracker.presentation.shared.component.primitive.CenterRow


@Composable
fun HeaderSection(){
    CenterRow {
        // TODO: Image or Icon Display
        Text(
            text = "[  ] Item Details",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        CenterColumn (modifier = Modifier
            .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
            .background(Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = "Current Stocks",
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            )
            Text(
                text = "240 units",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}