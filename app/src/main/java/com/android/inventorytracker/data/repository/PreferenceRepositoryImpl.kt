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

    override fun saveProfile(businessName: String, drink: String, firstItem: String, supplier: String) =
        BusinessProfile.saveProfile(context, businessName, drink, firstItem, supplier)
    override fun getBusinessName(): String? =
        BusinessProfile.getBusinessName(context)
    override fun verifySecurityAnswers(drink: String, firstItem: String, supplier: String): Boolean =
        BusinessProfile.verifySecurityAnswers(context, drink, firstItem, supplier)
}
