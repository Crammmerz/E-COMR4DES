package com.android.inventorytracker.presentation.onboarding

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.notification_permission_request.NotificationPermissionRequest
import com.android.inventorytracker.presentation.onboarding.component.AuthenticationSetup
import com.android.inventorytracker.presentation.onboarding.component.BusinessProfileSetup
import com.android.inventorytracker.presentation.onboarding.component.FeatureHighlights
import com.android.inventorytracker.presentation.onboarding.component.FinalConfirmation
import com.android.inventorytracker.presentation.onboarding.component.IntroScreen
import com.android.inventorytracker.presentation.onboarding.viewmodel.OnboardingViewModel
import com.android.inventorytracker.services.notification.NotificationHelper.canPostNotifications

enum class OnboardingPage { Page1, Page2, Page3, Page4, Page5, Page6 }

@Composable
fun Onboarding(
    onDismiss: () -> Unit,
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    var currentPage by rememberSaveable { mutableStateOf(OnboardingPage.Page1) }

    val isNotificationPermissionNeeded = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !canPostNotifications(context)
    var canProceed by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(100.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 100.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (currentPage) {
                OnboardingPage.Page1 -> IntroScreen()
                OnboardingPage.Page2 -> FeatureHighlights()
                OnboardingPage.Page3 -> NotificationPermissionRequest(modifier = Modifier.fillMaxSize(), showCancel = false)
                OnboardingPage.Page4 -> BusinessProfileSetup(onValidityChange = {canProceed = it})
                OnboardingPage.Page5 -> AuthenticationSetup()
                OnboardingPage.Page6 -> FinalConfirmation()
            }
        }

        Button(
            onClick = {
                when (currentPage) {
                    OnboardingPage.Page1 -> currentPage = OnboardingPage.Page2
                    OnboardingPage.Page2 -> currentPage = if (isNotificationPermissionNeeded) OnboardingPage.Page3 else OnboardingPage.Page4
                    OnboardingPage.Page3 -> currentPage = OnboardingPage.Page4
                    OnboardingPage.Page4 -> currentPage = OnboardingPage.Page5
                    OnboardingPage.Page5 -> currentPage = OnboardingPage.Page6
                    OnboardingPage.Page6 -> onDismiss()
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd),
            enabled = canProceed
        ) {
            Text(
                text = when (currentPage) {
                    OnboardingPage.Page6 -> "Start Managing Inventory"
                    else -> "Next >"
                }
            )
        }
        if (currentPage != OnboardingPage.Page1) {
            Button(
                onClick = {
                    when (currentPage) {
                        OnboardingPage.Page2 -> currentPage = OnboardingPage.Page1
                        OnboardingPage.Page3 -> currentPage = if (isNotificationPermissionNeeded) OnboardingPage.Page2 else OnboardingPage.Page1
                        OnboardingPage.Page4 -> currentPage = OnboardingPage.Page3
                        OnboardingPage.Page5 -> currentPage = OnboardingPage.Page4
                        OnboardingPage.Page6 -> currentPage = OnboardingPage.Page5
                        else -> {}
                    }
                },
                modifier = Modifier.align(Alignment.BottomStart),
                enabled = canProceed
            ) {
                Text(text = "< Back")
            }
        }

        TextButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { onDismiss() }
        ) {
            Text("Skip >>")
        }
    }
}
