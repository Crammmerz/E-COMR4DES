package com.android.inventorytracker.presentation.popup.itemdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThresholdField(){
    Text(
        text = "Threshold",
        color = Color.Gray,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp
    )
    Row (horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        BasicTextField(
            value = "null",
            onValueChange = { null },
            modifier = Modifier
                .weight(1f)
                .background(Color.LightGray)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(5.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            textStyle = TextStyle(fontSize = 15.sp)
        )
        Text(
            text = "unit",
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp
        )
    }
}