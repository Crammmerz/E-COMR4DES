package com.android.inventorytracker.services.notification

import android.Manifest
import android.app.Notification
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.inventorytracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun buildNotification(channelId: String, title: String, message: String, priority: Int = NotificationCompat.PRIORITY_DEFAULT): Notification {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.sharp_add_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(priority)
            .setAutoCancel(true)
            .build()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun postNotification(id: Int, notification: Notification) {
        try {
            NotificationManagerCompat.from(context).notify(id, notification)
        } catch (e: SecurityException) {
            // handle missing permission
        }
    }
}
