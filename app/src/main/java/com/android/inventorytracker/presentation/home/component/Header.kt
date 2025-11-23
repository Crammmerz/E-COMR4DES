package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.inventorytracker.presentation.shared.viewmodel.TimeViewModel
import com.android.inventorytracker.ui.theme.Ochre


@Composable
fun Header(){
    val timeViewModel: TimeViewModel = viewModel()
    Text(
        text = "Welcome to Lumi Cafe",
        color = Ochre,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    )
    Text(
        text = timeViewModel.getDayAndDate(),
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp
    )
    Text(
        text = "Quick Actions",
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        modifier = Modifier.padding(vertical = 5.dp)
    )
}