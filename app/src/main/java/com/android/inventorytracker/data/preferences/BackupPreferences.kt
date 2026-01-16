package com.android.inventorytracker.data.preferences

import android.content.Context
import android.content.SharedPreferences

object BackupPreferences {
    private const val PREF_NAME = "backup_prefs"
    private const val KEY_LAST_BACKUP = "last_backup_timestamp"
    private const val KEY_CURRENT_DB_NAME = "current_db_name"
    private const val DEFAULT_DB_NAME = "inventory.db"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setLastBackupTimestamp(context: Context, timestamp: Long) {
        getPreferences(context).edit().putLong(KEY_LAST_BACKUP, timestamp).apply()
    }

    fun getLastBackupTimestamp(context: Context): Long {
        return getPreferences(context).getLong(KEY_LAST_BACKUP, 0L)
    }

    fun setCurrentDbName(context: Context, name: String) {
        // We use commit() to ensure the name is written to disk BEFORE the app process is killed.
        getPreferences(context).edit().putString(KEY_CURRENT_DB_NAME, name).commit()
    }

    fun getCurrentDbName(context: Context): String {
        return getPreferences(context).getString(KEY_CURRENT_DB_NAME, DEFAULT_DB_NAME) ?: DEFAULT_DB_NAME
    }
}
