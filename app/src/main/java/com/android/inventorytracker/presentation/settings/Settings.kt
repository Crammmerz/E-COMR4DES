package com.android.inventorytracker.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.presentation.settings.component.Security

@Composable
fun Setting() {
    Column (modifier = Modifier.fillMaxSize()) {
        Security()
    }
}