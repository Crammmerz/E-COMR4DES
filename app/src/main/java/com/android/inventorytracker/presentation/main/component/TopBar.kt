package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.presentation.main.viewmodel.MainViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans

@Composable
fun TopBar(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val showNotif by mainViewModel.showNotif.collectAsState()

    Surface(
        // Ginamit ang Pure White para umangat laban sa Beige background ng app
        color = Palette.PureWhite,
        tonalElevation = 2.dp, // Nagdadagdag ng kaunting lalim sa Material3
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(elevation = 4.dp) // Shadow para "mangibabaw" sa content habang nag-scroll
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- MENU TOGGLE ---
            IconButton(
                onClick = { mainViewModel.setNavBar(!mainViewModel.showNavBar.value) }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle Navigation",
                    tint = Palette.DarkBrown
                )
            }

            Spacer(Modifier.weight(1f))

            // --- NOTIFICATIONS & APP TITLE ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = { mainViewModel.setNotif(!showNotif) }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        tint = Palette.AccentBeigePrimary, // Mas matingkad na beige para sa active feel
                        contentDescription = "Notifications"
                    )
                }

                Text(
                    text = "Inventory Tracking",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontWeight = FontWeight.Bold, // Bold para mas prominent ang title
                        fontSize = 17.sp,
                        color = Palette.DarkBeigeText,
                        letterSpacing = (-0.3).sp
                    )
                )
            }
        }
    }
}