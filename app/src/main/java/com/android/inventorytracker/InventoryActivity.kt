package com.android.inventorytracker

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.presentation.login.Login
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.main.Main
import com.android.inventorytracker.presentation.notification_permission_request.NotificationPermissionRequest
import com.android.inventorytracker.presentation.onboarding.Onboarding
import com.android.inventorytracker.presentation.onboarding.viewmodel.OnboardingViewModel
import com.android.inventorytracker.services.notification.NotificationHelper.canPostNotifications
import com.android.inventorytracker.ui.theme.InventoryTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val onBoardingViewModel: OnboardingViewModel = hiltViewModel()
            val loginViewModel: LoginViewModel = hiltViewModel()

            var showNotifRequest by remember { mutableStateOf(true) }

            val isNotificationPermissionNeeded =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        showNotifRequest &&
                        !canPostNotifications(context)

            val isOnboardingEnabled =
                onBoardingViewModel.onboardingEnabled.collectAsState().value

            val isLoggedIn = loginViewModel.loginState == LoginState.LOGGED_IN
            val isAuthEnabled = loginViewModel.authEnabled

            InventoryTrackerTheme {
                when {
                    isOnboardingEnabled -> Onboarding(
                        onDismiss = { onBoardingViewModel.toggleOnboarding(false) }
                    )

                    isNotificationPermissionNeeded -> NotificationPermissionRequest(
                        modifier = Modifier.fillMaxSize(),
                        onDismiss = {
                            // para hindi na ulit lumabas sa next recomposition
                            showNotifRequest = false
                        }
                    )

                    isAuthEnabled && !isLoggedIn -> Login()

                    else -> Main()
                }
            }
        }
    }
}
