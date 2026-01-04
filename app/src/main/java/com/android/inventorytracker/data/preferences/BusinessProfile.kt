package com.android.inventorytracker.data.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.core.content.edit

object BusinessProfile {
    private const val PREF_NAME = "business_profile_prefs"
    private const val KEY_BUSINESS_NAME = "business_name"
    private const val KEY_FAVORITE_DRINK = "favorite_drink"
    private const val KEY_FIRST_ITEM_SOLD = "first_item_sold"
    private const val KEY_FIRST_SUPPLIER = "first_supplier"

    private fun getPrefs(context: Context) =
        EncryptedSharedPreferences.create(
            PREF_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    // Save business profile info
    fun saveProfile(context: Context, businessName: String, drink: String, firstItem: String, supplier: String) {
        getPrefs(context).edit().apply {
            putString(KEY_BUSINESS_NAME, businessName)
            putString(KEY_FAVORITE_DRINK, drink)
            putString(KEY_FIRST_ITEM_SOLD, firstItem)
            putString(KEY_FIRST_SUPPLIER, supplier)
            apply()
        }
    }

    // Get business name
    fun getBusinessName(context: Context): String? {
        return getPrefs(context).getString(KEY_BUSINESS_NAME, null)
    }

    // Verify security answers (for reset flow)
    fun verifySecurityAnswers(context: Context, drink: String, firstItem: String, supplier: String): Boolean {
        val prefs = getPrefs(context)
        val storedDrink = prefs.getString(KEY_FAVORITE_DRINK, null)
        val storedItem = prefs.getString(KEY_FIRST_ITEM_SOLD, null)
        val storedSupplier = prefs.getString(KEY_FIRST_SUPPLIER, null)

        return storedDrink.equals(drink, ignoreCase = true) &&
                storedItem.equals(firstItem, ignoreCase = true) &&
                storedSupplier.equals(supplier, ignoreCase = true)
    }

    // Clear all profile data (factory reset)
    fun clearProfile(context: Context) {
        getPrefs(context).edit { clear() }
    }
}
