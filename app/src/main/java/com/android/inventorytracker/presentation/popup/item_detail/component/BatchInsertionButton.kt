package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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