package com.android.inventorytracking.presentation.layout

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracking.presentation.elements.LeftButton
import com.android.inventorytracking.presentation.elements.LeftColumn

@Composable
fun NavBar(bgColor: Color, modifier: Modifier = Modifier) {
    Surface(
        color = bgColor,
        tonalElevation = 10.dp,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.225f)// fixed width for sidebar
    ) {
        LeftColumn (
            Modifier
                .padding(20.dp)
        ) {
            NavElements()
        }
    }
}

@Composable
fun NavElements(){
    Text(
        modifier = Modifier.padding(bottom = 15.dp),
        text = "📦 Lumi Cafe",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
    )

    LeftButton(
        modifier = Modifier.fillMaxHeight(0.1f),
        label = "🏠 Home",
        bgColor = Color.Transparent,
        contentColor = Color.White
    ) {

    }

    LeftButton(
        modifier = Modifier.fillMaxHeight(0.1f),
        label = "📦 Inventory",
        bgColor = Color.Transparent,
        contentColor = Color.White
    ) {

    }
}

