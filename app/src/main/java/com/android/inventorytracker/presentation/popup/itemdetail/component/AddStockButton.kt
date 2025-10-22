package com.android.inventorytracker.presentation.popup.itemdetail.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.android.inventorytracker.presentation.shared.component.primitive.CenterButton

@Composable
fun AddStockButton(){
    CenterButton(
        modifier = Modifier.fillMaxWidth(),
        label = "+ Add Stock",
        bgColor = Color.Black,
        onClick = {})
}