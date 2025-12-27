package com.android.inventorytracker

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.presentation.login.Login
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.ui.theme.InventoryTrackerTheme
import com.android.inventorytracker.presentation.main.Main
import com.android.inventorytracker.presentation.notification_permission_request.NotificationPermissionRequest
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.services.notification.NotificationHelper.canPostNotifications

@AndroidEntryPoint
class InventoryActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loginViewModel: LoginViewModel = hiltViewModel()
            val showNotificationRequest = rememberSaveable { mutableStateOf(true) }

            // Mas malinis na logic:
            val isLoggedIn = loginViewModel.loginState == LoginState.LOGGED_IN
            val isAuthEnabled = loginViewModel.authEnabled
            val context = LocalContext.current

            InventoryTrackerTheme {
                when {
                    // 1. Kung kailangan ng Login (Enabled ang Auth) at hindi pa logged in:
                    isAuthEnabled && !isLoggedIn -> Login()

                    // 2. Kung naka-disable ang Auth O Logged in na ang user:
                    else -> Main()
                }

                // Notification handling
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    showNotificationRequest.value && !canPostNotifications(context)
                ) {
                    NotificationPermissionRequest(
                        modifier = Modifier.fillMaxSize(),
                        onDismiss = { showNotificationRequest.value = false }
                    )
                }
            }
        }
    }
}