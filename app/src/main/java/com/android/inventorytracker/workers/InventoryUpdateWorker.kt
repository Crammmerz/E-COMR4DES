package com.android.inventorytracker.workers

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.android.inventorytracker.InventoryActivity
import com.android.inventorytracker.R
import com.android.inventorytracker.services.notification.AppChannel
import com.android.inventorytracker.services.notification.NotificationHelper

class InventoryUpdateWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val UNIQUE_NAME = "inventory_update_worker"
        const val INPUT_TITLE = "title"
        const val INPUT_TEXT = "text"
        const val NOTIF_ID = 2001
    }

    private val channelId = AppChannel.INFO.id // or decide based on logic

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        // Optionally show a temporary foreground notification while running
        val fgNotif = NotificationHelper.buildNotification(
            applicationContext,
            AppChannel.INFO.id,
            applicationContext.getString(R.string.channel_name_info),
            applicationContext.getString(R.string.channel_desc_info),
            ongoing = true
        )
        setForegroundAsync(
            ForegroundInfo(NOTIF_ID, fgNotif)
        )

        return try {
            // YOUR background update logic here (network, DB sync)
            // e.g., fetch remote inventory, update local DB

            // After successful work, post a user-visible notification if needed
            val title = inputData.getString(INPUT_TITLE) ?: "Inventory update"
            val text = inputData.getString(INPUT_TEXT) ?: "Inventory was synced"
            val notif = NotificationHelper.buildNotification(
                applicationContext,
                AppChannel.INFO.id,
                title,
                text,
                intent = Intent(applicationContext, InventoryActivity::class.java),
                ongoing = false
            )
            NotificationHelper.notify(applicationContext, NOTIF_ID + 1, notif)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
