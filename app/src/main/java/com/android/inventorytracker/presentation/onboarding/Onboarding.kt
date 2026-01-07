package com.android.inventorytracker.presentation.onboarding

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.notification_permission_request.NotificationPermissionRequest
import com.android.inventorytracker.presentation.onboarding.component.*
import com.android.inventorytracker.presentation.onboarding.viewmodel.OnboardingViewModel
import com.android.inventorytracker.services.notification.NotificationHelper.canPostNotifications
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette

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

    // iOS Colors
    val iosBorderColor = Color(0xFFE5E5EA)
    val iosBlue = Color(0xFF007AFF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // CONTENT AREA
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (currentPage) {
                OnboardingPage.Page1 -> IntroScreen(onGetStartedClick = {
                    currentPage = OnboardingPage.Page2
                })
                OnboardingPage.Page2 -> FeatureHighlights()
                OnboardingPage.Page3 -> NotificationPermissionRequest(modifier = Modifier.fillMaxSize(), showCancel = false)
                OnboardingPage.Page4 -> BusinessProfileSetup(onValidityChange = { canProceed = it })
                OnboardingPage.Page5 -> AuthenticationSetup()
                OnboardingPage.Page6 -> FinalConfirmation()
            }
        }

        // NAVIGATION BUTTONS: Hidden on Page 1
        if (currentPage != OnboardingPage.Page1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 60.dp, vertical = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // --- BACK BUTTON (iOS Inspired White) ---
                OutlinedButton(
                    onClick = {
                        when (currentPage) {
                            OnboardingPage.Page2 -> currentPage = OnboardingPage.Page1
                            OnboardingPage.Page3 -> currentPage = OnboardingPage.Page2
                            OnboardingPage.Page4 -> currentPage = if (isNotificationPermissionNeeded) OnboardingPage.Page3 else OnboardingPage.Page2
                            OnboardingPage.Page5 -> currentPage = OnboardingPage.Page4
                            OnboardingPage.Page6 -> currentPage = OnboardingPage.Page5
                            else -> {}
                        }
                    },
                    modifier = Modifier
                        .height(45.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Palette.DarkBeigeText
                    ),
                    border = BorderStroke(1.dp, iosBorderColor)
                ) {
                    Text(
                        text = "Back",
                        style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    )
                }

                // --- NEXT BUTTON (iOS Inspired White) ---
                OutlinedButton(
                    onClick = {
                        when (currentPage) {
                            OnboardingPage.Page2 -> currentPage = if (isNotificationPermissionNeeded) OnboardingPage.Page3 else OnboardingPage.Page4
                            OnboardingPage.Page3 -> currentPage = OnboardingPage.Page4
                            OnboardingPage.Page4 -> currentPage = OnboardingPage.Page5
                            OnboardingPage.Page5 -> currentPage = OnboardingPage.Page6
                            OnboardingPage.Page6 -> onDismiss()
                            else -> {}
                        }
                    },
                    modifier = Modifier
                        .height(45.dp)
                        .width(140.dp),
                    enabled = canProceed,
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Palette.DarkBeigeText,
                        disabledContentColor = Palette.DarkBeigeText.copy(alpha = 0.3f)
                    ),
                    border = BorderStroke(1.dp, if (canProceed) iosBorderColor else iosBorderColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = if (currentPage == OnboardingPage.Page6) "Finish" else "Next",
                        style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                }
            }
        }

        // SKIP BUTTON (Top Right)
        TextButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp),
            onClick = { onDismiss() }
        ) {
            Text(
                text = "Skip",
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Palette.DarkBeigeText.copy(alpha = 0.5f)
                )
            )
        }
    }
}