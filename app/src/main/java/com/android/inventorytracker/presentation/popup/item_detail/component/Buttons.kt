package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.presentation.shared.component.primitive.CenterButton
import com.android.inventorytracker.ui.theme.Ochre

@Composable
fun BatchInsertionButton(onClick: () -> Unit){
    CenterButton(
        modifier = Modifier.fillMaxWidth(),
        label = "+ Add Stock",
        bgColor = Ochre,
        onClick = onClick
    )
}

@Composable
fun RemoveStockButton(enabled: Boolean = true, onClick: () -> Unit){
    CenterButton(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(5.dp)
            ),
        label = "- Remove Stock",
        bgColor = Color.White,
        contentColor = Color.Black,
        onClick = onClick,
        enabled = enabled
    )
}