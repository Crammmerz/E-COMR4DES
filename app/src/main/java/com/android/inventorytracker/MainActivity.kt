package com.android.inventorytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.inventorytracker.ui.theme.InventoryTrackerTheme
import com.android.inventorytracking.presentation.MainLayout

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventoryTrackerTheme {
                MainLayout()
            }
        }
    }
}