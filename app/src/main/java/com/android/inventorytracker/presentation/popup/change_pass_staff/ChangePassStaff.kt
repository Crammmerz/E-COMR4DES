package com.android.inventorytracker.presentation.popup.change_pass_staff

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.UserEntity
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import kotlinx.coroutines.launch


@Composable
fun ChangePassStaff(
    onDismiss: () -> Unit,
    onSubmit: suspend (UserEntity) -> Boolean
) {
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPass by rememberSaveable { mutableStateOf("") }

    var validNewPass by rememberSaveable { mutableStateOf(false) }
    var validConfirmPass by rememberSaveable { mutableStateOf(false) }

    val valid = validNewPass && validConfirmPass
    val isMatch = newPassword == confirmPass

    var successChange by rememberSaveable { mutableStateOf<Boolean?>(null) }

    val scope = rememberCoroutineScope()

    val focusNewPass = remember { FocusRequester() }
    val focusConfirmPass = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusNewPass.requestFocus()
    }

    fun doSubmit() {
        if (valid && isMatch) {
            scope.launch {
                val success = onSubmit(UserEntity(username = "staff", passwordHash = newPassword, role = "STAFF"))
                successChange = success
                if(success) onDismiss()
            }
        }
    }

    DialogHost(modifier = Modifier.height(300.dp).width(400.dp), onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Staff Password Change",
                style = MaterialTheme.typography.titleLarge
            )

            PasswordField(
                fieldModifier = Modifier.focusRequester(focusNewPass),
                value = newPassword,
                onValueChange = { newPassword = it },
                header = "New Password",
                onValidityChange = { validNewPass = it },
                onDone = { if(isMatch) focusConfirmPass.requestFocus() }
            )

            PasswordField(
                fieldModifier = Modifier.focusRequester(focusConfirmPass),
                value = confirmPass,
                onValueChange = { confirmPass = it },
                header = "Confirm Password",
                onValidityChange = { validConfirmPass = it },
                onDone = { doSubmit() }
            )
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
