package com.android.inventorytracking.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.android.inventorytracking.presentation.layout.ContentArea
import com.android.inventorytracking.presentation.layout.NavBar
import com.android.inventorytracking.presentation.layout.TopBar


@Composable
fun MainScreen(paddingValues: PaddingValues) {
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        TopBar(bgColor = Color.White)
        Row(modifier = Modifier.fillMaxSize()) {
            NavBar()
            ContentArea()
        }
    }
}
