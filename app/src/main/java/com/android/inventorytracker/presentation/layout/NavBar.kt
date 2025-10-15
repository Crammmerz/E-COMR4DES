package com.android.inventorytracking.presentation.layout

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracking.presentation.elements.LeftButton
import com.android.inventorytracking.presentation.elements.LeftColumn
import com.android.inventorytracking.presentation.screens.Content

@Composable
fun NavBar(
    bgColor: Color,
    modifier: Modifier = Modifier,
    onContentChange: (Content) -> Unit // ✅ fix type here
) {
    Surface(
        color = bgColor,
        tonalElevation = 10.dp,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.225f)
    ) {
        LeftColumn(
            Modifier.padding(10.dp)
        ) {
            NavElements(onContentChange)
        }
    }
}

@Composable
fun NavElements(onContentChange: (Content) -> Unit) {
    var selected by remember { mutableStateOf(Content.Home) }

    Text(
        modifier = Modifier.padding(all = 10.dp),
        text = "📦 Lumi Cafe",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
    )

    NavButton("\t\t🏠 Home", selected == Content.Home) {
        selected = Content.Home
        onContentChange(Content.Home)
    }

    NavButton("\t\t📦 Inventory", selected == Content.Inventory) {
        selected = Content.Inventory
        onContentChange(Content.Inventory)
    }
}

@Composable
fun NavButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) Color.Black.copy(alpha = 0.2f) else Color.Transparent

    LeftButton(
        modifier = Modifier.fillMaxHeight(0.075f),
        label = label,
        bgColor = bgColor,
        contentColor = Color.White,
        onClick = onClick
    )
}


