package com.android.inventorytracker.presentation.main.component

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.main.viewmodel.Content
import com.android.inventorytracker.presentation.main.viewmodel.MainViewModel
import com.android.inventorytracker.ui.theme.GoogleSans

/* ---------- COLORS ---------- */
private val NavBackground = Color(0xFF796254)
private val ItemHighlight = Color.White.copy(alpha = 0.15f)

/* ---------- NAV BAR ---------- */
@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel = hiltViewModel(),
    isOpen: Boolean,
) {
    val current by mainViewModel.currentContent.collectAsState()

    AnimatedVisibility(
        visible = isOpen,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(300)
        ) + fadeIn(),
        exit = slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(300)
        ) + fadeOut()
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .width(240.dp)
                .background(NavBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "MENU",
                modifier = Modifier.padding(start = 14.dp, bottom = 12.dp),
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = GoogleSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.4f)
                )
            )

            NavButton(
                label = "Home",
                icon = Icons.Rounded.Home,
                selected = current == Content.Home
            ) { mainViewModel.setContent(Content.Home) }

            NavButton(
                label = "Inventory",
                icon = Icons.Rounded.Inventory2,
                selected = current == Content.Inventory
            ) { mainViewModel.setContent(Content.Inventory) }

            if (loginViewModel.userRole == UserRole.ADMIN) {
                NavButton(
                    label = "Settings",
                    icon = Icons.Rounded.Settings,
                    selected = current == Content.Setting
                ) { mainViewModel.setContent(Content.Setting) }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- LOGOUT BUTTON WITH RESET LOGIC ---
            NavButton(
                label = "Log Out",
                icon = Icons.Rounded.Logout,
                selected = false
            ) {
                // 1. I-reset muna ang UI content sa Home para sa susunod na login
                mainViewModel.setContent(Content.Home)

                // 2. Tawagin ang logout function para ma-clear ang session
                loginViewModel.logout()
            }
        }
    }
}

/* ---------- NAV BUTTON ---------- */
@Composable
fun NavButton(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (selected) ItemHighlight else Color.Transparent,
        label = "NavItemBg"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (label == "Log Out") Color.White else Color.White,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = GoogleSans,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 16.sp,
                color = if (label == "Log Out") Color.White else Color.White
            )
        )
    }
}