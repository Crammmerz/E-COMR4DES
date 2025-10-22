package com.android.inventorytracker.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.inventorytracker.presentation.main.component.ContentSection
import com.android.inventorytracker.presentation.main.component.NavBar
import com.android.inventorytracker.presentation.main.component.TopBar


@Composable
fun MainScreen(paddingValues: PaddingValues) {
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        TopBar()
        Row(modifier = Modifier.weight(1f)) {
            NavBar()
            ContentSection()
        }
    }
}
