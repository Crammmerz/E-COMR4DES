package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.presentation.shared.component.primitive.CenterButton

@Composable
fun RemoveStockButton(){
    CenterButton(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(5.dp) // 👈 Rounded corners
            ),
        label = "- Remove Stock",
        bgColor = Color.White,
        contentColor = Color.Black,
        onClick = {}
    )
}