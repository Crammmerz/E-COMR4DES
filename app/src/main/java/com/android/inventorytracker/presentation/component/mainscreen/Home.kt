package com.android.inventorytracking.presentation.layouts


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.inventorytracker.Ochre
import com.android.inventorytracker.presentation.viewmodel.TimeViewModel
import com.android.inventorytracking.presentation.elements.CenterButton

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(modifier: Modifier = Modifier) {
    val timeViewModel: TimeViewModel = viewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "[  ] Welcome to Lumi Cafe",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Text(
            text = "Time: ${timeViewModel.getTimestamp()}",
            color = Color.Black.copy(alpha = 0.8f),
            fontSize = 10.sp
        )

        Text(
            text = "Quick Actions",
            color = Color.Black,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        QuickActions()
    }
}

@Composable
fun QuickActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        Buttons("Place Holder", Modifier.weight(1f)){}
        Buttons("Place Holder", Modifier.weight(1f)){}
        Buttons("Place Holder", Modifier.weight(1f)){}
        Buttons("Place Holder", Modifier.weight(1f)){}
    }
}



@Composable
fun Buttons(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CenterButton(
        modifier = modifier
            .fillMaxHeight(),
        label = label,
        bgColor = Color.White,
        contentColor = Ochre,
        onClick = onClick
    )
}