package com.android.inventorytracker.presentation.main.component

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.inventorytracker.Ochre
import com.android.inventorytracker.presentation.main.viewmodel.Content
import com.android.inventorytracker.presentation.main.viewmodel.ContentViewModel
import com.android.inventorytracker.presentation.shared.component.primitive.LeftButton
import com.android.inventorytracker.presentation.shared.component.primitive.LeftColumn

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    bgColor: Color = Ochre,
    contentViewModel: ContentViewModel = viewModel()
) {
    val highlight = Color.Black.copy(alpha = 0.25f)
    val default = Color.Transparent

    Surface(
        color = bgColor,
        tonalElevation = 10.dp,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.225f)
    ) {
        LeftColumn(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "📦 Lumi Cafe",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )

            NavButton(
                label = "🏠 Home",
                bgColor = if (contentViewModel.currentContent == Content.Home) highlight else default
            ) {
                contentViewModel.currentContent = Content.Home
            }

            NavButton(
                label = "📦 Inventory",
                bgColor = if (contentViewModel.currentContent == Content.Inventory) highlight else default
            ) {
                contentViewModel.currentContent = Content.Inventory
            }
        }
    }
}

@Composable
fun NavButton(
    label: String,
    bgColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    LeftButton(
        label = label,
        bgColor = bgColor,
        contentColor = Color.White,
        onClick = onClick,
        modifier = Modifier
            .fillMaxHeight(0.075f)
    )
}

