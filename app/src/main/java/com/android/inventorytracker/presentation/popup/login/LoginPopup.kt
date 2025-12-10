package com.android.inventorytracker.presentation.popup.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField

@Composable
fun LoginPopup(
    userRole: UserRole,
    onDismiss: () -> Unit,
    onLogin: (username: String, password: String, userRole: String) -> Unit,
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var validUsername by rememberSaveable { mutableStateOf(false) }
    var validPassword by rememberSaveable { mutableStateOf(false) }

    val focusUsername = remember { FocusRequester() }
    val focusPassword = remember { FocusRequester() }

    focusUsername.requestFocus()

    val header = when (userRole) {
        UserRole.ADMIN -> "Admin Login"
        UserRole.STAFF -> "Staff Login"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(header) },
        text = {
            Column {
                StringField(
                    value = username,
                    onValueChange = { username = it },
                    header = "Username",
                    placeholder = "Enter username",
                    modifier = Modifier.fillMaxWidth(),
                    maxLength = 99,
                    inputModifier = Modifier.focusRequester(focusUsername),
                    onValidationChange = { validUsername = it },
                    onDone = {
                        focusPassword.requestFocus()
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
                PasswordField(
                    value = password,
                    onValueChange = { password = it },
                    header = "Password",
                    modifier = Modifier.fillMaxWidth().focusRequester(focusPassword),
                    onValidityChange = { validPassword = it },
                    onDone = {
                        onLogin(username, password, userRole.name)
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if(validUsername && validPassword)
                onLogin(username, password, userRole.name)
            }) {
                Text("Login")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}