package com.android.inventorytracker.presentation.popup.change_pass_admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.UserEntity
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import kotlinx.coroutines.launch

@Composable
fun ChangePassAdmin(
    onDismiss: () -> Unit,
    onSubmit: suspend (UserEntity, newPassword: String) -> Boolean
) {
    var oldPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPass by rememberSaveable { mutableStateOf("") }

    var validOldPass by rememberSaveable { mutableStateOf(false) }
    var validNewPass by rememberSaveable { mutableStateOf(false) }
    var validConfirmPass by rememberSaveable { mutableStateOf(false) }

    val valid = validNewPass && validOldPass && validConfirmPass
    val isMatch = newPassword == confirmPass

    var successChange by rememberSaveable { mutableStateOf<Boolean?>(null) }

    val scope = rememberCoroutineScope()

    val focusOldPass = remember { FocusRequester() }
    val focusNewPass = remember { FocusRequester() }
    val focusConfirmPass = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusOldPass.requestFocus()
    }

    fun doSubmit() {
        if (valid && isMatch) {
            scope.launch {
                val success = onSubmit(
                    UserEntity(
                        username = "admin",
                        passwordHash = oldPassword,
                        role = "ADMIN"
                    ),
                    newPassword
                )
                successChange = success
                if(success) onDismiss()
            }
        }
    }

    DialogHost(modifier = Modifier.size(400.dp), onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Admin Password Change",
                style = MaterialTheme.typography.titleLarge
            )

            PasswordField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                header = "Old Password",
                onValidityChange = { validOldPass = it },
                onDone = { focusNewPass.requestFocus() }
            )

            PasswordField(
                value = newPassword,
                onValueChange = { newPassword = it },
                header = "New Password",
                onValidityChange = { validNewPass = it },
                onDone = { focusConfirmPass.requestFocus() }
            )

            PasswordField(
                value = confirmPass,
                onValueChange = { confirmPass = it },
                header = "Confirm Password",
                onValidityChange = { validConfirmPass = it },
                onDone = { doSubmit() }
            )

            if(!isMatch) Text("Passwords do not match")
            if(successChange == false) Text("Invalid old password")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                CancelButton { onDismiss() }

                Spacer(modifier = Modifier.width(8.dp))

                ConfirmButton(
                    text = "Change",
                    onClick = { doSubmit() }
                )
            }
        }
    }
}
