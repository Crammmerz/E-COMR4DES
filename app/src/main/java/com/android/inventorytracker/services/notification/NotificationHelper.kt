package com.android.inventorytracker.services.notification

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.android.inventorytracker.R

object NotificationHelper {
    private const val DEFAULT_NOTIFICATION_ID = 1000

    fun buildNotification(
        context: Context,
        channelId: String,
        title: String,
        text: String,
        intent: Intent? = null,
        ongoing: Boolean = false
    ): Notification {
        val pending = intent?.let {
            PendingIntent.getActivity(
                context,
                channelId.hashCode(),
                it,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.sharp_add_24)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pending)
            .setOngoing(ongoing)
            .setAutoCancel(!ongoing)
            .build()
    }

    // Simple permission guard that is safe on older OS versions
    fun canPostNotifications(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Safe notify that checks permission before posting
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun safeNotify(context: Context, id: Int, notification: Notification) {
        if (canPostNotifications(context)) {
            NotificationManagerCompat.from(context).notify(id, notification)
        } else {
            // Permission not granted — choose no-op or log. Do not call notify.
        }
    }
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun notify(context: Context, id: Int = DEFAULT_NOTIFICATION_ID, notification: Notification) {
        NotificationManagerCompat.from(context).notify(id, notification)
    }

    fun cancel(context: Context, id: Int = DEFAULT_NOTIFICATION_ID) {
        NotificationManagerCompat.from(context).cancel(id)
    }
}
