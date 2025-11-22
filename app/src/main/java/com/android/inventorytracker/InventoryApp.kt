package com.android.inventorytracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.android.inventorytracker.services.notification.NotificationChannels
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InventoryApp : Application() {
    companion object {
        const val CHANNEL_ID = "inventory_channel"
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val outOfStock = NotificationChannel(
                NotificationChannels.OUT_OF_STOCK,
                getString(R.string.channel_name_out_of_stock),
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = getString(R.string.channel_desc_out_of_stock) }

            val lowStock = NotificationChannel(
                NotificationChannels.LOW_STOCK,
                getString(R.string.channel_name_low_stock),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = getString(R.string.channel_desc_low_stock) }

            val expired = NotificationChannel(
                NotificationChannels.EXPIRED,
                getString(R.string.channel_name_expired),
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = getString(R.string.channel_desc_expired) }

            val nearExpiry = NotificationChannel(
                NotificationChannels.NEAR_EXPIRY,
                getString(R.string.channel_name_near_expiry),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = getString(R.string.channel_desc_near_expiry) }

            nm.createNotificationChannels(
                listOf(outOfStock, expired, lowStock, nearExpiry)
            )
        }
    }
}