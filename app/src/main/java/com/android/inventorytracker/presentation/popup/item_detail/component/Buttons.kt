package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.ui.theme.Ochre

@Composable
fun BatchInsertionButton(onClick: () -> Unit){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .background(Ochre)
    ) {
        Text(text = "Add Stock")
    }
}

@Composable
fun RemoveStockButton(enabled: Boolean = true, onClick: () -> Unit){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(5.dp)
            ),
        onClick = onClick,
        enabled = enabled
    ) {
        Text(
            text = "Deduct Stock",
        )
    }
}