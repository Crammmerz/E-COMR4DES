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
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.popup.login.LoginPopup

@Composable
fun Login(
    userRole: UserRole,
    onSetUserRole: (UserRole) -> Unit,
    onLogin: (username: String, password: String, userRole: String) -> Unit,
    isRoleAuthEnabled: Boolean
) {
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
            onSetUserRole(UserRole.ADMIN)
            showDialog = true
        }) {
            Text("Admin Login")
        }
        if(isRoleAuthEnabled){
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                onSetUserRole(UserRole.STAFF)
                showDialog = true
            }) {
                Text("User Login")
            }
        }

        if (showDialog) {
            LoginPopup(
                userRole = userRole,
                onDismiss = { showDialog = false },
                onLogin = onLogin
            )
        }
    }
}
