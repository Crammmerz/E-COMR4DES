package com.android.inventorytracker.presentation.notification_permission_request

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.services.notification.NotificationHelper.canPostNotifications

@Composable
fun NotificationPermissionRequest(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    // Runtime guard: no-op on pre-API 33 devices
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Remember permission state and a saved UI visibility flag (caller may still control)
    var isGranted by remember {
        mutableStateOf(canPostNotifications(context))
    }

    // Re-check permission on resume (user could change in Settings)
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isGranted = canPostNotifications(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isGranted = granted
        if (granted) {
            onDismiss()
        }
    }

    // Helper to open app-level or channel-level notification settings
    val openNotificationSettings = {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    // Show UI only when permission not granted
    if (!isGranted) {
        Column(
            modifier = modifier.background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Turn On Notifications")
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Get updates about critical inventory changes.")
            Spacer(modifier = Modifier.height(12.dp))

            // Primary action: request permission unless permanently denied
            ConfirmButton ("Turn on Notification", onClick = {
                openNotificationSettings()
                onDismiss()
            })

            Spacer(modifier = Modifier.width(8.dp))

            CancelButton ("No, Thanks", onClick = { onDismiss() })
        }
    }
}