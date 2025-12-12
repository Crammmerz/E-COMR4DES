package com.android.inventorytracker.presentation.popup.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.UserEntity
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import kotlinx.coroutines.launch

@Composable
fun LoginPopup(
    userRole: UserRole,
    onDismiss: () -> Unit,
    onLogin: suspend (user: UserEntity) -> Boolean,
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var validUsername by rememberSaveable { mutableStateOf(false) }
    var validPassword by rememberSaveable { mutableStateOf(false) }

    var loginSuccess by rememberSaveable { mutableStateOf<Boolean?>(null) }

    val valid = validUsername && validPassword

    val scope = rememberCoroutineScope()

    val focusUsername = remember { FocusRequester() }
    val focusPassword = remember { FocusRequester() }

    val header = when (userRole) {
        UserRole.ADMIN -> "Admin Login"
        UserRole.STAFF -> "Staff Login"
    }

    LaunchedEffect(Unit) {
        focusUsername.requestFocus()
    }

    fun onSubmit(){
        if(valid){
            val user = UserEntity(username = username, passwordHash = password, role = userRole.name)
            scope.launch {
                val success = onLogin(user)
                loginSuccess = success
                if(success) onDismiss()
            }
        }
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
                    onDone = { focusPassword.requestFocus() },
                )
                Spacer(modifier = Modifier.height(8.dp))
                PasswordField(
                    value = password,
                    onValueChange = { password = it },
                    header = "Password",
                    modifier = Modifier.fillMaxWidth().focusRequester(focusPassword),
                    onValidityChange = { validPassword = it },
                    onDone = { onSubmit() }
                )
                if(loginSuccess == false){
                    Text("Invalid user credential")
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSubmit() }) {
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