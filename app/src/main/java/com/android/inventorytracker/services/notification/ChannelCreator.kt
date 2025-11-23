package com.android.inventorytracker.services.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object ChannelCreator {

    // Public entry: create channels once (e.g., Application.onCreate)
    fun createAll(context: Context) {
        val nm = context.getSystemService(NotificationManager::class.java) ?: return
        nm.createNotificationChannels(buildChannels(context))
    }

    // Build list of configured NotificationChannel objects

    private fun buildChannels(context: Context): List<NotificationChannel> =
        AppChannel.entries.map { channelConfig ->
            NotificationChannel(
                channelConfig.id,
                context.getString(channelConfig.nameRes),
                channelConfig.importance
            ).apply {
                description = context.getString(channelConfig.descRes)
                setShowBadge(true)
                configureChannelSpecifics(this, channelConfig)
            }
        }

    // Keep special-case configuration separate and obvious
    private fun configureChannelSpecifics(channel: NotificationChannel, config: AppChannel) {
        when (config) {
            AppChannel.CRITICAL -> {
                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(0L, 300L, 150L, 300L)
                channel.importance = NotificationManager.IMPORTANCE_HIGH
            }
            AppChannel.WARNING -> {
                channel.importance = NotificationManager.IMPORTANCE_DEFAULT
            }
            AppChannel.INFO, AppChannel.SILENT -> {
                channel.importance = NotificationManager.IMPORTANCE_LOW
            }
        }
    }
}