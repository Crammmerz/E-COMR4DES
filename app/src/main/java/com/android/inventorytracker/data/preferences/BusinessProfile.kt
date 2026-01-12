package com.android.inventorytracker.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object BusinessProfile {
    private const val PREF_NAME = "business_profile_prefs"
    private const val KEY_BUSINESS_NAME = "business_name"
    private const val KEY_PIN = "pin"
    private const val KEY_SECURITY_PHRASE = "security_phrase"

    @Volatile
    private var sharedPrefs: SharedPreferences? = null

    // This uses the non-deprecated MasterKey.Builder
    private fun getPrefs(context: Context): SharedPreferences {
        return sharedPrefs ?: synchronized(this) {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                PREF_NAME,
                masterKey, // Modern MasterKey object
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ).also { sharedPrefs = it }
        }
    }

    fun saveSecurityRecovery(context: Context, pin: String, phrase: String) {
        getPrefs(context).edit().apply {
            putString(KEY_PIN, pin)
            putString(KEY_SECURITY_PHRASE, phrase)
            apply()
        }
    }

    fun saveBusinessName(context: Context, businessName: String) {
        getPrefs(context).edit().apply {
            putString(KEY_BUSINESS_NAME, businessName)
            apply()
        }
    }

    fun getBusinessName(context: Context): String? {
        return getPrefs(context).getString(KEY_BUSINESS_NAME, null)
    }

    fun verifySecurityAnswers(context: Context, inputPin: String, inputPhrase: String): Boolean {
        val prefs = getPrefs(context)
        val savedPin = prefs.getString(KEY_PIN, null)
        val savedPhrase = prefs.getString(KEY_SECURITY_PHRASE, null)

        // Safety check: ensure values exist before comparing
        if (savedPin == null || savedPhrase == null) return false

        return savedPin == inputPin || savedPhrase.equals(inputPhrase, ignoreCase = true)
    }

    fun clearProfile(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}