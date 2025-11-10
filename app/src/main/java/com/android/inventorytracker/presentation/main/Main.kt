package com.android.inventorytracker.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.inventorytracker.data.local.database.InventoryDatabase
import com.android.inventorytracker.presentation.main.component.ContentSection
import com.android.inventorytracker.presentation.main.component.NavBar
import com.android.inventorytracker.presentation.main.component.TopBar


@Composable
fun Main(db: InventoryDatabase) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar()
        Row(modifier = Modifier.weight(1f)) {
            NavBar()
            ContentSection(db = db)
        }
    }
}
