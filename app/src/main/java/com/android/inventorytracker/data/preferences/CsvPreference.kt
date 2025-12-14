package com.android.inventorytracker.data.preferences

import android.content.Context
import androidx.core.content.edit

object CsvPreference {
    private const val PREF_NAME = "csv_import_prefs"
    private const val KEY_SHOW_CONFIRMATION = "show_confirmation"

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setShowConfirmation(context: Context, show: Boolean) {
        getPrefs(context).edit { putBoolean(KEY_SHOW_CONFIRMATION, show) }
    }

    fun shouldShowConfirmation(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_SHOW_CONFIRMATION, true)
    }
}
