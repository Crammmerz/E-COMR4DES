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
    fun buildNotification(
        context: Context,
        channelId: String,
        title: String = "Inventory",
        text: String,
        subtext: List<String> = emptyList(),
        priority: Int,
        intent: Intent? = null,
        ongoing: Boolean = false,
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

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(priority)
            .setContentIntent(pending)
            .setOngoing(ongoing)
            .setAutoCancel(!ongoing)

        // Collect all lines into one InboxStyle
        val inboxStyle = NotificationCompat.InboxStyle()
        if (subtext.isNotEmpty()){
            subtext.forEach { it ->
                inboxStyle.addLine(it)
            }
        }

        if (subtext.isNotEmpty()){
            builder.setStyle(inboxStyle)
        }

        return builder.build()
    }

    fun canPostNotifications(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            true
        } else {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun safeNotify(context: Context, id: Int, notification: Notification) {
        if (canPostNotifications(context)) {
            NotificationManagerCompat.from(context).notify(id, notification)
        }
    }

    fun cancel(context: Context, id: Int) {
        NotificationManagerCompat.from(context).cancel(id)
    }

    fun channelImportanceToPriority(importance: Int) = when (importance) {
        android.app.NotificationManager.IMPORTANCE_HIGH -> NotificationCompat.PRIORITY_HIGH
        android.app.NotificationManager.IMPORTANCE_DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
        android.app.NotificationManager.IMPORTANCE_LOW -> NotificationCompat.PRIORITY_LOW
        else -> NotificationCompat.PRIORITY_MIN
    }
}
