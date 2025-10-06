package com.android.inventorytracking.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.android.inventorytracking.presentation.components.ContentArea
import com.android.inventorytracking.presentation.components.NavBar
import com.android.inventorytracking.presentation.components.SideBar


val LightBrown = Color(0xFFE5D7BD)
val Bistre = Color(0xFF3e2723)

@Composable
fun MainLayout() {
    Column(modifier = Modifier.fillMaxSize()) {
        NavBar(bgColor = Color.White)

        Row(modifier = Modifier.fillMaxSize()) {
            SideBar(Bistre)
            ContentArea(LightBrown)
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
        MainLayout()
    }
}
