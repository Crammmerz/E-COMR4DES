package com.android.inventorytracker.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.android.inventorytracker.data.repository.PreferencesRepository
import com.android.inventorytracker.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val preferenceRepo: PreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _authEnabled = MutableStateFlow(preferenceRepo.isAuthEnabled())
    val authEnabled: StateFlow<Boolean> = _authEnabled
    private val _roleAuthEnabled = MutableStateFlow(preferenceRepo.isRoleAuthEnabled())
    val roleAuthEnabled: StateFlow<Boolean> = _roleAuthEnabled
    fun toggleAuth(enabled: Boolean) {
        preferenceRepo.setAuthEnabled(enabled)
        _authEnabled.value = enabled
    }

    fun toggleRoleAuth(enabled: Boolean) {
        preferenceRepo.setRoleAuthEnabled(enabled)
        _roleAuthEnabled.value = enabled
    }

    suspend fun register(username: String, rawPassword: String, role: String) {
        try {
            userRepository.register(username, rawPassword, role)
        } catch (e: Exception) {
            // Handle registration error
        }
    }
    suspend fun updateUser(newPass: String, role: String): Boolean {
        return try {
            userRepository.updateUser(newPass, role)
        } catch (e: Exception) {
            false
        }
    }

    fun getCount(role: String): Flow<Int> {
        return userRepository.getCount(role)
            .catch { emit(0) }
    }
}