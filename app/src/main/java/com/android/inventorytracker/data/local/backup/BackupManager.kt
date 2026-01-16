package com.android.inventorytracker.data.local.backup

import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.inventorytracker.data.local.database.InventoryDatabase
import com.android.inventorytracker.data.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: InventoryDatabase,
    private val preferencesRepository: PreferencesRepository
) {
    companion object {
        private const val TAG = "BackupManager"
        private const val BACKUP_FILE_NAME = "inventory_backup.db"
        private const val TEMP_BACKUP_FILE_NAME = "inventory_backup.db.tmp"
        private const val PENDING_RESTORE_FILE_NAME = "pending_restore.db"
        private const val DATABASE_NAME = "inventory.db"
    }

    /**
     * Performs a backup of the Room database.
     */
    fun backupDatabase(): Result<Unit> {
        return try {
            // Checkpoint WAL into main file
            database.openHelper.writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close()
            
            val dbFile = context.getDatabasePath(DATABASE_NAME)
            if (!dbFile.exists()) {
                return Result.failure(IOException("Database file not found"))
            }

            val backupDir = context.filesDir
            val tempFile = File(backupDir, TEMP_BACKUP_FILE_NAME)
            val backupFile = File(backupDir, BACKUP_FILE_NAME)

            if (!backupDir.exists() && !backupDir.mkdirs()) {
                return Result.failure(IOException("Could not create backup directory"))
            }
            
            FileInputStream(dbFile).use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            if (tempFile.renameTo(backupFile)) {
                preferencesRepository.setLastBackupTimestamp(System.currentTimeMillis())
                Result.success(Unit)
            } else {
                tempFile.delete()
                Result.failure(IOException("Failed to rename temp backup file"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Backup failed", e)
            Result.failure(e)
        }
    }

    fun exportBackup(uri: Uri): Result<Unit> {
        return try {
            val backupFile = File(context.filesDir, BACKUP_FILE_NAME)
            if (!backupFile.exists() || backupFile.length() == 0L) {
                backupDatabase().onFailure { return Result.failure(it) }
            }
            
            context.contentResolver.openOutputStream(uri)?.use { output ->
                FileInputStream(backupFile).use { input ->
                    input.copyTo(output)
                }
            } ?: return Result.failure(IOException("Could not open output stream for export"))
            
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Export failed", e)
            Result.failure(e)
        }
    }

    /**
     * Stages a backup file for restoration on next app startup.
     */
    fun importBackup(uri: Uri): Result<Unit> {
        return try {
            val pendingFile = File(context.filesDir, PENDING_RESTORE_FILE_NAME)
            
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(pendingFile).use { output ->
                    input.copyTo(output)
                }
            } ?: return Result.failure(IOException("Could not open input stream for import"))
            
            if (pendingFile.length() == 0L) {
                return Result.failure(IOException("Imported file is empty"))
            }

            Log.d(TAG, "Restore staged as pending_restore.db. App restart required.")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Import failed", e)
            Result.failure(e)
        }
    }

    fun isBackupAvailable(): Boolean {
        val file = File(context.filesDir, BACKUP_FILE_NAME)
        return file.exists() && file.length() > 0
    }
    
    fun getLastBackupTime(): Long {
        return preferencesRepository.getLastBackupTimestamp()
    }
}
