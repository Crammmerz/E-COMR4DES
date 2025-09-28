package com.android.inventorytracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracking.ui.theme.InventoryTrackingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventoryTrackingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(
        color = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = modifier
                .padding(16.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            Text(
                text = "Hello $name! where did you come from",
                color = Color.White,
                fontSize = 30.sp
            )
            Text(
                text = "Hands Up Gabriela",
                color = Color.White,
                fontSize = 30.sp
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "The Preview"
)
@Composable
fun GreetingPreview() {
    InventoryTrackingTheme {
        Greeting(
            "Crammm"
            )
    }
}