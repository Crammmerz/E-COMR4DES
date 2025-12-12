package com.android.inventorytracker.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.android.inventorytracker.data.local.entities.UserEntity
import com.android.inventorytracker.data.repository.PreferencesRepository
import com.android.inventorytracker.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    suspend fun updateUser(user: UserEntity, newPass: String): Boolean {
        return try {
            userRepository.updateUser(user, newPass)
        } catch (e: Exception) {
            false
        }
    }
    suspend fun updateUserStaff(user: UserEntity): Boolean {
        return try {
            userRepository.updateUserStaff(user)
        } catch (e: Exception) {
            false
        }
    }
}