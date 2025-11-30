package com.android.inventorytracker.data.repository

import android.content.Context
import com.android.inventorytracker.data.preferences.AuthPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    override fun setAuthEnabled(enabled: Boolean) =
        AuthPreferences.setAuthEnabled(context, enabled)
    override fun isAuthEnabled(): Boolean =
        AuthPreferences.isAuthEnabled(context)

    override fun setRoleAuthEnabled(enabled: Boolean) =
        AuthPreferences.setRoleAuthEnabled(context, enabled)
    override fun isRoleAuthEnabled(): Boolean =
        AuthPreferences.isRoleAuthEnabled(context)
}
