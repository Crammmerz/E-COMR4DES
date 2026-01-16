package com.android.inventorytracker

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.android.inventorytracker.services.notification.ChannelCreator
import com.android.inventorytracker.services.notification.NotificationHelper
import com.android.inventorytracker.services.notification.SyncScheduler
import dagger.hilt.android.HiltAndroidApp
import java.io.File
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
        // --- CRITICAL: RESTORE SWAP ---
        // We do this BEFORE super.onCreate() to ensure no Hilt singletons 
        // (like the Database) have been created yet.
        performRestoreSwapIfPending()
        
        super.onCreate()
        
        ChannelCreator.createAll(this)

        if (NotificationHelper.canPostNotifications(this)) {
            SyncScheduler.schedulePeriodicInventorySync(this)
        }
        SyncScheduler.schedulePeriodicBackup(this)
    }

    private fun performRestoreSwapIfPending() {
        try {
            val pendingFile = File(filesDir, "pending_restore.db")
            if (pendingFile.exists()) {
                val dbFile = getDatabasePath("inventory.db")
                
                // 1. Delete the current DB and ALL its journal files (-wal, -shm)
                // This is the only way to prevent SQLite from "recovering" an empty DB
                deleteDatabase("inventory.db")
                
                // 2. Move the pending file to the live location
                if (pendingFile.copyTo(dbFile, overwrite = true).exists()) {
                    pendingFile.delete()
                    Log.d("InventoryApp", "Restoration swap successful! New DB size: ${dbFile.length()}")
                }
            }
        } catch (e: Exception) {
            Log.e("InventoryApp", "Restore swap failed during boot", e)
        }
    }
}
