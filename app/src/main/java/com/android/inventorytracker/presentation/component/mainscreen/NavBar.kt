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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.inventorytracker.Ochre
import com.android.inventorytracker.presentation.viewmodel.Content
import com.android.inventorytracker.presentation.viewmodel.ContentViewModel
import com.android.inventorytracking.presentation.elements.LeftButton
import com.android.inventorytracking.presentation.elements.LeftColumn

@Composable
fun NavBar(
    bgColor: Color = Ochre,
    contentViewModel: ContentViewModel = viewModel()
) {
    val highlight = Color.Black.copy(0.25f)
    val default = Color.Transparent

    Surface(
        color = bgColor,
        tonalElevation = 10.dp,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.225f)
    ) {
        LeftColumn(
            Modifier.padding(10.dp)
        ) {
            Text(
                modifier = Modifier.padding(all = 10.dp),
                text = "📦 Lumi Cafe",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            NavButton("\t\t🏠 Home", bgColor = if (contentViewModel.currentContent == Content.Home) highlight else default) {
                contentViewModel.currentContent = Content.Home
            }

            NavButton("\t\t📦 Inventory", bgColor = if (contentViewModel.currentContent == Content.Inventory) highlight else default) {
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
        modifier = Modifier.fillMaxHeight(0.075f),
        label = label,
        bgColor = bgColor,
        contentColor = Color.White,
        onClick = onClick
    )
}


