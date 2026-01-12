package com.android.inventorytracker.data.repository

interface PreferencesRepository {
    fun setAuthEnabled(enabled: Boolean)
    fun isAuthEnabled(): Boolean
    fun setRoleAuthEnabled(enabled: Boolean)
    fun isRoleAuthEnabled(): Boolean

    fun setCsvConfirmation(show: Boolean)
    fun shouldShowConfirmation(): Boolean

    fun setShowOnboarding(show: Boolean)
    fun shouldShowOnboarding(): Boolean

    fun saveBusinessName(businessName: String)
    fun getBusinessName(): String?
    fun saveSecurityRecovery(inputPin: String, inputPhrase: String)
    fun verifyRecoveryInfo(inputPin: String, inputPhrase: String): Boolean
}

