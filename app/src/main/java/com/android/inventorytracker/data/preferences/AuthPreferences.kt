package com.android.inventorytracker.data.preferences

import android.content.Context
import androidx.core.content.edit

object AuthPreferences {
    private const val PREF_NAME = "auth_prefs"
    private const val KEY_AUTH_ENABLED = "auth_enabled"
    private const val KEY_ROLE_AUTH_ENABLED = "role_auth_enabled"

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


    fun setAuthEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit { putBoolean(KEY_AUTH_ENABLED, enabled) }
    }

    fun isAuthEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_AUTH_ENABLED, false)
    }

    fun setRoleAuthEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit { putBoolean(KEY_ROLE_AUTH_ENABLED, enabled) }
    }

    fun isRoleAuthEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_ROLE_AUTH_ENABLED, false)
    }

    fun clearAuthFlag(context: Context) {
        getPrefs(context).edit { remove(KEY_AUTH_ENABLED) }
    }
}
