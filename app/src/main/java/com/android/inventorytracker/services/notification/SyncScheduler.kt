package com.android.inventorytracker.services.notification

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.android.inventorytracker.workers.InventoryUpdateWorker
import java.util.concurrent.TimeUnit

object SyncScheduler {
    fun schedulePeriodicInventorySync(context: Context) {
        val appCtx = context.applicationContext

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicReq = PeriodicWorkRequestBuilder<InventoryUpdateWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .addTag("inventory_sync_periodic")
            .build()

        WorkManager.getInstance(appCtx)
            .enqueueUniquePeriodicWork(
                InventoryUpdateWorker.UNIQUE_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicReq
            )
    }
}
