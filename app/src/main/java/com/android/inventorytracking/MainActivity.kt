package com.android.inventorytracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.inventorytracking.ui.theme.InventoryTrackingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventoryTrackingTheme {
                /**
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                }
                **/
            }
        }
    }
}

