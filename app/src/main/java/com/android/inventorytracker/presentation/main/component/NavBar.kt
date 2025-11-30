package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.presentation.main.viewmodel.Content
import com.android.inventorytracker.presentation.main.viewmodel.MainViewModel
import com.android.inventorytracker.ui.theme.Ochre

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val current by viewModel.currentContent.collectAsState()
    val highlight = Color.Black.copy(alpha = 0.25f)
    val default = Color.Transparent

    Surface(color = Ochre, modifier = modifier.fillMaxHeight()) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("📦 Lumi Cafe")
            NavButton("🏠 Home", bgColor = if (current == Content.Home) highlight else default) { viewModel.setContent(Content.Home) }
            NavButton("📦 Inventory", bgColor = if (current == Content.Inventory) highlight else default) { viewModel.setContent(Content.Inventory) }
            NavButton("⚙️ Setting", bgColor = if (current == Content.Setting) highlight else default) { viewModel.setContent(Content.Setting) }
        }
    }
}



