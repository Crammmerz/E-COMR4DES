package com.android.inventorytracker.presentation.notification_permission_request

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.services.notification.NotificationHelper.canPostNotifications
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun NotificationPermissionRequest(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    showCancel: Boolean = true
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isGranted by remember {
        mutableStateOf(canPostNotifications(context))
    }

    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isGranted = canPostNotifications(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    val openNotificationSettings = {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    if (!isGranted) {
        Column(
            modifier = modifier // Fixed: use the passed modifier
                .fillMaxSize()
                .padding(horizontal = 120.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // --- HEADER (FIXED TEXTSTYLE) ---
            Text(
                text = "Inventory Tracker",
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = Palette.DarkBeigeText,
                    letterSpacing = (-2.5).sp
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = Palette.DarkBeigeText.copy(alpha = 0.1f),
                        modifier = Modifier.size(160.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1.2f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Turn On Notifications",
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = Color.Black
                        )
                    )

                    Text(
                        text = "Get real-time updates about critical inventory changes and low stock alerts.",
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontSize = 18.sp,
                            lineHeight = 26.sp,
                            color = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // FIXED: Removed 'contentColor' if your custom ConfirmButton doesn't support it
                        ConfirmButton(
                            text = "Turn on Notifications",
                            containerColor = Palette.ButtonBeigeBase,
                            onClick = {
                                openNotificationSettings()
                                onDismiss()
                            }
                        )

                        if (showCancel) {
                            Spacer(modifier = Modifier.width(16.dp))

                            // UNIFORM BUTTON: No, Thanks
                            Button(
                                onClick = { onDismiss() },
                                shape = RoundedCornerShape(32.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Palette.ButtonBeigeBase,
                                    contentColor = Palette.ButtonDarkBrown
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                                modifier = Modifier.height(56.dp)
                            ) {
                                Text(
                                    text = "No, Thanks",
                                    style = TextStyle(
                                        fontFamily = GoogleSans,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}