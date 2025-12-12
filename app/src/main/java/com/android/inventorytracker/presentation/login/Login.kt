package com.android.inventorytracker.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.popup.login.LoginPopup

@Composable
fun Login(
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val userRole = loginViewModel.userRole
    val isRoleAuthEnabled = loginViewModel.roleAuthEnabled
    val header = if(isRoleAuthEnabled) "Select Login Type" else "Login"
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(header, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            loginViewModel.updateUserRole(UserRole.ADMIN)
            showDialog = true
        }) {
            Text("Admin Login")
        }
        if(isRoleAuthEnabled){
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                loginViewModel.updateUserRole(UserRole.STAFF)
                showDialog = true
            }) {
                Text("User Login")
            }
        }

        if (showDialog) {
            LoginPopup(
                userRole = userRole,
                onDismiss = { showDialog = false },
                onLogin = loginViewModel::onLogin
            )
        }
    }
}
