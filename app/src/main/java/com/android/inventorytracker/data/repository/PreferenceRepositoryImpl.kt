package com.android.inventorytracker.data.repository

import android.content.Context
import com.android.inventorytracker.data.preferences.AuthPreferences
import com.android.inventorytracker.data.preferences.BusinessProfile
import com.android.inventorytracker.data.preferences.CsvPreference
import com.android.inventorytracker.data.preferences.OnboardingPreference
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

    override fun setCsvConfirmation(show: Boolean) =
        CsvPreference.setShowConfirmation(context, show)
    override fun shouldShowConfirmation(): Boolean =
        CsvPreference.shouldShowConfirmation(context)

    override fun setShowOnboarding(show: Boolean) =
        OnboardingPreference.setShowOnboarding(context, show)
    override fun shouldShowOnboarding(): Boolean =
        OnboardingPreference.shouldShowOnboarding(context)

    override fun saveBusinessName(businessName: String) =
        BusinessProfile.saveBusinessName(context, businessName)
    override fun getBusinessName(): String? =
        BusinessProfile.getBusinessName(context)
    override fun saveSecurityRecovery(inputPin: String, inputPhrase: String) =
        BusinessProfile.saveSecurityRecovery(context, inputPin, inputPhrase)
    override fun verifyRecoveryInfo(inputPin: String, inputPhrase: String): Boolean =
        BusinessProfile.verifySecurityAnswers(context, inputPin = inputPin, inputPhrase = inputPhrase)
}
