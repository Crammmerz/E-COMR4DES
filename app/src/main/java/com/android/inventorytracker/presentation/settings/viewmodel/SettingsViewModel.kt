package com.android.inventorytracker.presentation.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.repository.PreferencesRepository
import com.android.inventorytracker.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _authEnabled = MutableStateFlow(preferencesRepository.isAuthEnabled())
    val authEnabled: StateFlow<Boolean> = _authEnabled
    private val _roleAuthEnabled = MutableStateFlow(preferencesRepository.isRoleAuthEnabled())
    val roleAuthEnabled: StateFlow<Boolean> = _roleAuthEnabled
    fun toggleAuth(enabled: Boolean) {
        preferencesRepository.setAuthEnabled(enabled)
        _authEnabled.value = enabled
    }
    fun toggleRoleAuth(enabled: Boolean) {
        preferencesRepository.setRoleAuthEnabled(enabled)
        _roleAuthEnabled.value = enabled
    }

    fun changePassword(user: String, password: String, role: String) {
        viewModelScope.launch {
            userRepository.changePassword(user, password, role)
        }
    }
}