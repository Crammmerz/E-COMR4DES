package com.android.inventorytracking.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.android.inventorytracking.presentation.screens.RootScreen

val Sand = Color(0xFFE1D9C5)
val Stone = Color(0xFFAE9372)
val LightBrown = Color(0xFFE5D7BD)
val Bistre = Color(0xFF3e2723)
val Ochre = Color(0xFF7F4B30)


@Composable
fun MainLayout() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        RootScreen(innerPadding)
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
        MainLayout()
    }
}
