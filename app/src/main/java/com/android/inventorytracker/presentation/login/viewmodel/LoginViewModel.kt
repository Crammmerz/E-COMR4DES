package com.android.inventorytracker.presentation.login.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.android.inventorytracker.data.local.entities.UserEntity
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.data.repository.PreferencesRepository
import com.android.inventorytracker.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val authEnabled: Boolean get() = preferencesRepository.isAuthEnabled()
    val roleAuthEnabled: Boolean get() = preferencesRepository.isRoleAuthEnabled()

    var loginState by mutableStateOf(LoginState.LOGGED_OUT)
        private set

    var userRole by mutableStateOf(UserRole.ADMIN)
        private set

    suspend fun onLogin(user: UserEntity): Boolean {
        val success = userRepository.authenticate(user)
        loginState = if (success) LoginState.LOGGED_IN else LoginState.LOGGED_OUT
        return success
    }

    fun updateUserRole(role: UserRole){
        this.userRole = role
    }

    // --- IDAGDAG ITONG FUNCTION ---
    fun logout() {
        loginState = LoginState.LOGGED_OUT
    }
}