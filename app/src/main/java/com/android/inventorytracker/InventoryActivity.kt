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
import com.android.inventorytracker.presentation.onboarding.viewmodel.OnboardingViewModel
import com.android.inventorytracker.services.notification.NotificationHelper.canPostNotifications
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.android.inventorytracker.presentation.onboarding.Onboarding

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

            val isNotificationPermissionNeeded = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && showNotifRequest && !canPostNotifications(context)
            val isOnboardingEnabled = onBoardingViewModel.onboardingEnabled.collectAsState().value
            val isLoggedIn = loginViewModel.loginState == LoginState.LOGGED_IN
            val isAuthEnabled = loginViewModel.authEnabled


            InventoryTrackerTheme {
                when {
                    isOnboardingEnabled -> Onboarding(onDismiss = { onBoardingViewModel.toggleOnboarding(false) })
                    isNotificationPermissionNeeded -> NotificationPermissionRequest(modifier = Modifier.fillMaxSize(), onDismiss = { showNotifRequest = false })
                    isAuthEnabled && !isLoggedIn -> Login()
                    else -> Main()
                }
            }
        }
    }
}