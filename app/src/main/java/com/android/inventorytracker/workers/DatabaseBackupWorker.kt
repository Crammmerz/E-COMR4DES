package com.android.inventorytracker.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.inventorytracker.data.local.backup.BackupManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException

@HiltWorker
class DatabaseBackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val backupManager: BackupManager
) : CoroutineWorker(context, params) {

    companion object {
        const val UNIQUE_NAME = "database_backup_worker"
        private const val TAG = "DatabaseBackupWorker"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Database backup worker started")
        
        val result = backupManager.backupDatabase()
        
        return result.fold(
            onSuccess = {
                Log.d(TAG, "Database backup completed successfully")
                Result.success()
            },
            onFailure = { error ->
                Log.e(TAG, "Database backup failed: ${error.message}")
                if (error is IOException && error.message?.contains("No space left on device", ignoreCase = true) == true) {
                    // Disk full - maybe don't retry immediately
                    Result.failure()
                } else {
                    Result.retry()
                }
            }
        )
    }
}
