package com.android.inventorytracker.presentation.popup.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.android.inventorytracker.data.local.entities.UserEntity
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans
import kotlinx.coroutines.launch

@Composable
fun LoginPopup(
    userRole: UserRole,
    onDismiss: () -> Unit,
    onLogin: suspend (user: UserEntity) -> Boolean,
) {
    // --- States ---
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var validUsername by rememberSaveable { mutableStateOf(false) }
    var validPassword by rememberSaveable { mutableStateOf(false) }
    var loginSuccess by rememberSaveable { mutableStateOf<Boolean?>(null) }

    // --- Logic & Focus ---
    val valid = validUsername && validPassword
    val scope = rememberCoroutineScope()
    val focusUsername = remember { FocusRequester() }
    val focusPassword = remember { FocusRequester() }

    val header = when (userRole) {
        UserRole.ADMIN -> "Admin Login"
        UserRole.STAFF -> "Staff Login"
    }

    fun doLogin() {
        if (valid) {
            val user = UserEntity(
                username = username,
                passwordHash = password,
                role = userRole.name
            )
            scope.launch {
                val success = onLogin(user)
                loginSuccess = success
                if (success) onDismiss()
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .width(460.dp) // Uniform width sa ChangePassAdmin
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            color = Palette.PopupSurface, // Ginagamit ang Palette theme
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp), // Uniform padding
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // --- HEADER ---
                Text(
                    text = header,
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Palette.DarkBeigeText
                )

                // --- INPUT FIELDS ---
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    StringField(
                        inputModifier = Modifier.focusRequester(focusUsername),
                        value = username,
                        onValueChange = { username = it },
                        header = "Username",
                        placeholder = "Enter username",
                        modifier = Modifier.fillMaxWidth(),
                        maxLength = 99,
                        onValidationChange = { validUsername = it },
                        onDone = { focusPassword.requestFocus() }
                    )

                    PasswordField(
                        fieldModifier = Modifier.focusRequester(focusPassword),
                        value = password,
                        onValueChange = { password = it },
                        header = "Password",
                        modifier = Modifier.fillMaxWidth(),
                        onValidityChange = { validPassword = it },
                        onDone = { doLogin() }
                    )
                }

                // --- ERROR MESSAGE ---
                if (loginSuccess == false) {
                    Text(
                        text = "Invalid user credentials",
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        ),
                        color = Color.Red
                    )
                }

                // --- ACTION BUTTONS ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CancelButton(onClick = onDismiss)

                    Spacer(modifier = Modifier.width(12.dp))

                    ConfirmButton(
                        text = "Login",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = valid,
                        onClick = { doLogin() }
                    )
                }
            }
        }
    }

    // Auto-focus sa username field pagkabukas ng popup
    LaunchedEffect(Unit) {
        focusUsername.requestFocus()
    }
}