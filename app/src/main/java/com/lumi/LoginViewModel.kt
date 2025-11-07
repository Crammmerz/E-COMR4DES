package com.lumi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Enum for user types
enum class UserType {
    ADMIN, USER
}

// Data class for login state
data class LoginState(
    val isLoggedIn: Boolean = false,
    val userType: UserType? = null,
    val errorMessage: String? = null
)

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    // Simulate login (replace with real API/database logic)
    fun login(userType: UserType, username: String, password: String) {
        viewModelScope.launch {
            // Hardcoded credentials for demo
            val isValid = when (userType) {
                UserType.ADMIN -> username == "admin" && password == "admin"
                UserType.USER -> username == "user" && password == "user"
            }

            if (isValid) {
                _loginState.value = LoginState(isLoggedIn = true, userType = userType, errorMessage = null)
            } else {
                _loginState.value = LoginState(isLoggedIn = false, userType = null, errorMessage = "Invalid credentials")
            }
        }
    }

    // Reset state (e.g., after logout)
    fun reset() {
        _loginState.value = LoginState()
    }
}