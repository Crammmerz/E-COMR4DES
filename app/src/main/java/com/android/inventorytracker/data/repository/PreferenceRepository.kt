package com.android.inventorytracker.data.repository

interface PreferencesRepository {
    fun setAuthEnabled(enabled: Boolean)
    fun isAuthEnabled(): Boolean
    fun setRoleAuthEnabled(enabled: Boolean)
    fun isRoleAuthEnabled(): Boolean

    fun setCsvConfirmation(show: Boolean)
    fun shouldShowConfirmation(): Boolean
}

