package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.presentation.shared.component.primitive.CenterButton
import com.android.inventorytracker.ui.theme.Ochre

@Composable
fun QuickActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        Buttons("Place Holder", Modifier.weight(1f)){}
        Buttons("Place Holder", Modifier.weight(1f)){}
        Buttons("Place Holder", Modifier.weight(1f)){}
        Buttons("Place Holder", Modifier.weight(1f)){}
    }
}

@Composable
fun Buttons(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CenterButton(
        modifier = modifier
            .fillMaxHeight(),
        label = label,
        bgColor = Color.White,
        contentColor = Ochre,
        onClick = onClick
    )
}