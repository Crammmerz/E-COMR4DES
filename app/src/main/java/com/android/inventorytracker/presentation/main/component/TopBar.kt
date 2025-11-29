package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.presentation.main.viewmodel.MainViewModel

@Composable
fun TopBar(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            IconButton(onClick = { mainViewModel.setNavBar(!mainViewModel.showNavBar.value)}) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle Navigation"
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = "Inventory Tracking 📦",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}

