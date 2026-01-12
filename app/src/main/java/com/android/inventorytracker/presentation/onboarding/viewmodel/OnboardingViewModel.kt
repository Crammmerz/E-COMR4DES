package com.android.inventorytracker.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import com.android.inventorytracker.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _onboardingEnabled = MutableStateFlow( preferencesRepository.shouldShowOnboarding())
    val onboardingEnabled: StateFlow<Boolean> = _onboardingEnabled

    fun setBusinessProfile(name: String) {
        preferencesRepository.saveBusinessName(name)
    }
    fun setSecurityRecovery(pin: String, phrase: String) {
        preferencesRepository.saveSecurityRecovery(pin, phrase)
    }
    fun toggleOnboarding(enabled: Boolean) {
        preferencesRepository.setShowOnboarding(enabled)
        _onboardingEnabled.value = enabled
    }
}