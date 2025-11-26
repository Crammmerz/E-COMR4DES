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

        val oneTimeReq = OneTimeWorkRequestBuilder<InventoryUpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("inventory_sync_one_time")
            .build()

        WorkManager.getInstance(appCtx)
            .enqueueUniqueWork(
                InventoryUpdateWorker.UNIQUE_NAME,
                ExistingWorkPolicy.REPLACE,
                oneTimeReq
            )

    }
}
