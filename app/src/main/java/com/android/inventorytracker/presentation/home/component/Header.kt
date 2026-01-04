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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.inventorytracker.presentation.home.viewmodel.HomeViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.TimeViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans

@Composable
fun Header(
    homeViewModel: HomeViewModel = hiltViewModel()
){
    val timeViewModel: TimeViewModel = viewModel()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = (homeViewModel.businessName ?: "Inventory Tracker").uppercase(),
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 34.sp,
                color = Palette.DarkBeigeText
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Quick Actions",
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Palette.DarkBeigeText
                )
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = timeViewModel.getDayAndDate(),
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Palette.LightBeigeText
                )
            )
        }
    }
}