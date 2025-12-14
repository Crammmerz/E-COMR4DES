package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.inventorytracker.presentation.shared.viewmodel.TimeViewModel
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun Header(){
    val timeViewModel: TimeViewModel = viewModel()

    // Use a Column to stack the elements vertically
    Column(modifier = Modifier.fillMaxWidth()) {

        // 1. Date/Time (Subtle, Light Beige Color, Regular Font Weight)
        Text(
            text = "Inventory Dashboard",
            style = TextStyle(
                fontWeight = FontWeight.SemiBold, // SemiBold for prominence
                fontSize = 34.sp,
                color = Palette.DarkBeigeText // Dark beige for prominence
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Quick Actions",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Palette.DarkBeigeText
                )
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = timeViewModel.getDayAndDate(),
                style = TextStyle(
                    fontWeight = FontWeight.Normal, // Regular
                    fontSize = 16.sp,
                    color = Palette.LightBeigeText // Use light beige for subtlety
                )
            )
        }
    }
}