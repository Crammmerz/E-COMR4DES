package com.android.inventorytracker.presentation.settings.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.popup.change_password.ChangePasswordPopup
import com.android.inventorytracker.presentation.settings.viewmodel.SettingsViewModel

@Composable
fun Security(viewModel: SettingsViewModel = hiltViewModel()){
    val isAuthEnabled by viewModel.authEnabled.collectAsState()
    val isRoleAuthEnabled by viewModel.roleAuthEnabled.collectAsState()
    var roleToChangePassword by remember { mutableStateOf<UserRole?>(null) }


    Text(text = "User Account & Security", style = MaterialTheme.typography.titleLarge)

    Spacer(modifier = Modifier.height(15.dp))
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Enable Authentication",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = isAuthEnabled,
            onCheckedChange = { checked -> viewModel.toggleAuth(checked) }
        )
    }
    if(isAuthEnabled){
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Role Authentication",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Checkbox(
                checked = isRoleAuthEnabled,
                onCheckedChange = { checked -> viewModel.toggleRoleAuth(checked) }
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Admin Password Change",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {roleToChangePassword = UserRole.ADMIN}) {
                Text(text = "Change Password")
            }
        }
        if(isRoleAuthEnabled){
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Staff Password Change",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {roleToChangePassword = UserRole.STAFF}) {
                    Text(text = "Change Password")
                }
            }
        }

        if (roleToChangePassword != null) {
            ChangePasswordPopup(
                role = roleToChangePassword!!,
                onDismiss = { roleToChangePassword = null },
                onConfirm = { user, password, role ->
                    viewModel.changePassword(user, password, role)
                    roleToChangePassword = null
                }
            )
        }
    }
}