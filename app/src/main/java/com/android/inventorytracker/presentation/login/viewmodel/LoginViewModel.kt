package com.android.inventorytracker.presentation.login.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.data.repository.PreferencesRepository
import com.android.inventorytracker.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

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

    fun onLogin(username: String, password: String, userRole: String) {
        viewModelScope.launch {
            val success = userRepository.login(username, password, userRole)
            loginState = if (success) LoginState.LOGGED_IN else LoginState.LOGGED_OUT
        }
    }

    fun updateUserRole(role: UserRole){
        this.userRole = role
    }
}
