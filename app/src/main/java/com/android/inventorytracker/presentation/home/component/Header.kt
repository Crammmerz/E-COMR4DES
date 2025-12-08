package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.inventorytracker.presentation.shared.viewmodel.TimeViewModel

@Composable
fun Header(){
    val timeViewModel: TimeViewModel = viewModel()
    Row(verticalAlignment = Alignment.CenterVertically){
        Text(
            text = timeViewModel.getDayAndDate(),
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
    }
    Text(
        text = "Quick Actions",
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
    )
}