package com.android.inventorytracker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.android.inventorytracker.services.notification.ChannelCreator
import com.android.inventorytracker.services.notification.NotificationHelper
import com.android.inventorytracker.services.notification.SyncScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class InventoryApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        // Create notification channels
        ChannelCreator.createAll(this)

        // Schedule periodic sync only if notifications are allowed
        if (NotificationHelper.canPostNotifications(this)) {
            SyncScheduler.schedulePeriodicInventorySync(this)
        }
    }
}
