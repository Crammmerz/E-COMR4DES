package com.android.inventorytracker.presentation.popup.change_password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost

@Composable
fun ChangePasswordPopup(
    role: UserRole,
    onDismiss: () -> Unit,
    onConfirm: (user: String, password: String, role: String) -> Unit
) {
    var password by rememberSaveable { mutableStateOf("") }
    var valid by rememberSaveable { mutableStateOf(false) }

    DialogHost(modifier = Modifier.size(300.dp), onDismissRequest = onDismiss) {
        Column (modifier = Modifier.padding(20.dp)) {
            Text(
                text = when (role) {
                        UserRole.ADMIN -> "Admin Password Change"
                        UserRole.STAFF -> "Staff Password Change"
                },
                style = MaterialTheme.typography.titleLarge
            )
            PasswordField(
                value = password,
                onValueChange = { password = it },
                header = "New Password",
                isValid = { valid = it },
                onDone = {
                    if (password.isNotEmpty()) {
                        when (role) {
                            UserRole.ADMIN -> onConfirm("admin", password, "ADMIN")
                            UserRole.STAFF -> onConfirm("staff", password, "STAFF")
                        }
                    }
                }
            )
            Row {
                CancelButton { onDismiss() }
                ConfirmButton (text = "Change") {
                    if(password.isNotEmpty() && valid){
                        when (role) {
                            UserRole.ADMIN -> onConfirm("admin", password, "ADMIN")
                            UserRole.STAFF -> onConfirm("staff", password, "STAFF")
                        }
                    }
                }
            }
        }
    }
}