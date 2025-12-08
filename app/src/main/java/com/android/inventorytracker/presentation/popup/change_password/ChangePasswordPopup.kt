package com.android.inventorytracker.presentation.popup.change_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    var isInputValid by rememberSaveable { mutableStateOf(false) }

    // 1. Logic Extraction: Define submission logic once
    fun submitAction() {
        if (password.isNotEmpty() && isInputValid) {
            // Centralize the role mapping logic
            val (username, roleString) = when (role) {
                UserRole.ADMIN -> "admin" to "ADMIN"
                UserRole.STAFF -> "staff" to "STAFF"
                // Fallback safety
            }
            onConfirm(username, password, roleString)
        }
    }

    DialogHost(modifier = Modifier.width(320.dp), onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.padding(24.dp),
            // 3. Layout: Add consistent spacing between elements
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                onValidityChange = { isInputValid = it },
                onDone = { submitAction() }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                // 4. Layout: Align buttons to the end (standard dialog UX)
                horizontalArrangement = Arrangement.End
            ) {
                CancelButton { onDismiss() }

                Spacer(modifier = Modifier.width(8.dp))

                ConfirmButton(
                    text = "Change",
                    onClick = { submitAction() }
                )
            }
        }
    }
}
