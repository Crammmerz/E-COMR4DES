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

    fun saveProfile(businessName: String, drink: String, firstItem: String, supplier: String)
    fun getBusinessName(): String?
    fun verifySecurityAnswers(drink: String, firstItem: String, supplier: String): Boolean
}

