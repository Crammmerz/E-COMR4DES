package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.main.viewmodel.Content
import com.android.inventorytracker.presentation.main.viewmodel.MainViewModel
import com.android.inventorytracker.ui.theme.Ochre

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel = hiltViewModel(),
    isOpen: Boolean,
    onDismiss: () -> Unit
) {
    val current by mainViewModel.currentContent.collectAsState()
    val highlight = Color.Black.copy(alpha = 0.25f)
    val default = Color.Transparent

    if (isOpen) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable { onDismiss() } // dismiss when tapped outside
            )

            Column (
                modifier = modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .background(Ochre)
                    .padding(10.dp),
            ) {
                NavButton(
                    label = "🏠 Home",
                    bgColor = if (current == Content.Home) highlight else default
                ) { mainViewModel.setContent(Content.Home) }
                NavButton(
                    label = "📦 Inventory",
                    bgColor = if (current == Content.Inventory) highlight else default
                ) { mainViewModel.setContent(Content.Inventory) }
                if(loginViewModel.userRole == UserRole.ADMIN){
                    NavButton(
                        label = "⚙️ Setting",
                        bgColor = if (current == Content.Setting) highlight else default
                    ) { mainViewModel.setContent(Content.Setting) }
                }
            }
        }
    }
}



