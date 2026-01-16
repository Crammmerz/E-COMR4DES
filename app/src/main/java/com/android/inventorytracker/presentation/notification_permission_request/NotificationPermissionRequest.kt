package com.android.inventorytracker.presentation.notification_permission_request

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
    onDismiss: () -> Unit
) {
    // 🔒 Guard: Android 13+ lang
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        onDismiss()
        return
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isGranted by remember {
        mutableStateOf(canPostNotifications(context))
    }

    // 🔄 Re-check kapag bumalik galing settings
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isGranted = canPostNotifications(context)
                if (isGranted) onDismiss()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val openNotificationSettings = {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    // 🚫 Wag mag-render kung granted na
    if (isGranted) return

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 120.dp, vertical = 60.dp)
    ) {

        // ---------- HEADER ----------
        Text(
            text = "StockWise",
            modifier = Modifier.align(Alignment.TopStart),
            style = TextStyle(
                fontFamily = GoogleSans,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Palette.DarkBeigeText,
                letterSpacing = (-2.5).sp
            )
        )

        // ---------- CONTENT ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ICON
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = Palette.DarkBeigeText.copy(alpha = 0.12f),
                modifier = Modifier.size(180.dp)
            )

            // TEXT + ACTIONS
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Turn On Notifications",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color.Black
                    )
                )

                Text(
                    text = "Stay informed with real-time alerts for low stock levels and critical inventory updates.",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontSize = 18.sp,
                        lineHeight = 26.sp,
                        color = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ---------- ACTION BUTTONS ----------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    ConfirmButton(
                        modifier = Modifier.weight(1f),
                        text = "Turn on Notifications",
                        containerColor = Palette.ButtonBeigeBase,
                        onClick = {
                            openNotificationSettings()
                        }
                    )

                    ConfirmButton(
                        modifier = Modifier.weight(1f),
                        text = "No, Thanks",
                        containerColor = Palette.ButtonBeigeBase,
                        onClick = { onDismiss() }
                    )
                }
            }
        }
    }
}
