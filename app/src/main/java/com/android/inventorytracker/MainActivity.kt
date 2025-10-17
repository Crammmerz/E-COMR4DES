package com.android.inventorytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.android.inventorytracker.ui.theme.InventoryTrackerTheme
import com.android.inventorytracking.presentation.screens.MainScreen

val Sand = Color(0xFFE1D9C5)
val Stone = Color(0xFFAE9372)
val LightBrown = Color(0xFFE5D7BD)
val Bistre = Color(0xFF3e2723)
val Ochre = Color(0xFF7F4B30)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventoryTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(innerPadding)
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "The Preview",
    device = "spec:width=900dp,height=550dp,dpi=420,isRound=false,orientation=landscape"
)
@Composable
fun MainPanelPreview() {
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainScreen(innerPadding)
        }
    }
}