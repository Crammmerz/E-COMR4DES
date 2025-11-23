package com.android.inventorytracker.presentation.home


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.presentation.home.component.Header
import com.android.inventorytracker.presentation.home.component.QuickActions

@Composable
fun Home(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(20.dp)) {
        Header()
        QuickActions()
    }
}
