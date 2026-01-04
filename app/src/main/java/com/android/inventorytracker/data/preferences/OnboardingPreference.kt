package com.android.inventorytracker.data.preferences

import android.content.Context
import androidx.core.content.edit

object OnboardingPreference {
    private const val PREF_NAME = "onboarding_preference"
    private const val KEY_SHOW_ONBOARDING = "show_onboarding"

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setShowOnboarding(context: Context, show: Boolean) {
        getPrefs(context).edit { putBoolean(KEY_SHOW_ONBOARDING, show) }
    }
    fun shouldShowOnboarding(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_SHOW_ONBOARDING, true)
    }
}