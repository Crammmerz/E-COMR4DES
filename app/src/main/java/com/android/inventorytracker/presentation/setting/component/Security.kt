package com.android.inventorytracker.presentation.setting.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.setting.viewmodel.SettingsViewModel

@Composable
fun Security(viewModel: SettingsViewModel = hiltViewModel()){
    val isAuthEnabled by viewModel.authEnabled.collectAsState()
    val isRoleAuthEnabled by viewModel.roleAuthEnabled.collectAsState()

    Text(text = "User Account & Security", style = MaterialTheme.typography.titleLarge)

    Spacer(modifier = Modifier.height(15.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
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
        Row(modifier = Modifier.fillMaxWidth()) {
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
    }
}