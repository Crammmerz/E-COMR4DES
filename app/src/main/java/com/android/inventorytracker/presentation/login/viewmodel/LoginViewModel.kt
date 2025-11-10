package com.android.inventorytracker.presentation.login.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {
    var loginState by mutableStateOf(LoginState.LOGGED_OUT)
        private set

    var userRole by mutableStateOf(UserRole.STAFF)
        private set
    fun onLogin(username: String, password: String){
        viewModelScope.launch {
            if(userRepository.login(
                    username = username,
                    rawPassword = password,
                    role = when(userRole){
                        UserRole.ADMIN -> "ADMIN"
                        UserRole.STAFF -> "STAFF"
                    }
                )) {
                loginState = LoginState.LOGGED_IN
            }
        }
    }
    fun updateLoginState(newState: LoginState) {
        this.loginState = newState
    }
    fun updateUserRole(role: UserRole){
        this.userRole = role
    }
}
