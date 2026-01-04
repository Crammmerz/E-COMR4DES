package com.android.inventorytracker.data.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.core.content.edit

object AuthPreferences {
    private const val PREF_NAME = "auth_prefs"
    private const val KEY_AUTH_ENABLED = "auth_enabled"
    private const val KEY_ROLE_AUTH_ENABLED = "role_auth_enabled"

    private fun getPrefs(context: Context) =
        EncryptedSharedPreferences.create(
            PREF_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

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
