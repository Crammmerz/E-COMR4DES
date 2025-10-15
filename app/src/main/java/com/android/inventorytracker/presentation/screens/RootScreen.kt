package com.android.inventorytracking.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.android.inventorytracking.presentation.Ochre
import com.android.inventorytracking.presentation.Sand
import com.android.inventorytracking.presentation.layout.ContentArea
import com.android.inventorytracking.presentation.layout.NavBar
import com.android.inventorytracking.presentation.layout.TopBar
import com.android.inventorytracking.presentation.layouts.Home
import com.android.inventorytracking.presentation.layouts.Inventory

enum class Content {
    Home, Inventory
}

@Composable
fun RootScreen(paddingValues: PaddingValues) {
    var currentContent by remember { mutableStateOf(Content.Home) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        TopBar(bgColor = Color.White)

        Row(modifier = Modifier.fillMaxSize()) {
            NavBar(Ochre) { selected ->
                currentContent = selected
            }

            ContentArea(Sand) {
                when (currentContent) {
                    Content.Home -> Home(Color.Transparent)
                    Content.Inventory -> Inventory(Color.Transparent)
                }
            }
        }
    }
}
