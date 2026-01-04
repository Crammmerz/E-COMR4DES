package com.android.inventorytracker.presentation.popup.create_acc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette
import kotlinx.coroutines.launch


@Composable
fun CreateAccPopup(
    role: String,
    onDismiss: (Boolean) -> Unit,
    onSubmit: suspend (username: String, rawPassword: String, role: String) -> Unit
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPass by rememberSaveable { mutableStateOf("") }

    var validUsername by remember { mutableStateOf(false) }
    var validPassword by remember { mutableStateOf(false) }
    var validConfirm by remember { mutableStateOf(false) }

    val valid = validUsername && validPassword && validConfirm
    val isMatch = password == confirmPass

    val scope = rememberCoroutineScope()
    val focusUsername = remember { FocusRequester() }
    val focusPassword = remember { FocusRequester() }
    val focusConfirmPass = remember { FocusRequester() }

    val header = if(role == "ADMIN") "Create Admin Account" else "Create Staff Account"

    fun doSubmit(){
        scope.launch {
            onSubmit(username, password, role)
            onDismiss(true)
        }
    }

    Dialog(
        onDismissRequest = { onDismiss(false) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .width(460.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp), // Consistent with Item Removal
            color = Palette.PopupSurface, // Matches other popups
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
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

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    StringField(
                        fieldModifier = Modifier.focusRequester(focusUsername),
                        value = username,
                        onValueChange = { username = it },
                        header = "Username",
                        placeholder = "Enter username",
                        onValidityChange = { validUsername = it },
                        onDone = { focusPassword.requestFocus() }
                    )
                    PasswordField(
                        fieldModifier = Modifier.focusRequester(focusPassword),
                        value = password,
                        onValueChange = { password = it },
                        header = "Password",
                        onValidityChange = { validPassword = it },
                        onDone = { focusConfirmPass.requestFocus() }
                    )
                    PasswordField(
                        fieldModifier = Modifier.focusRequester(focusConfirmPass),
                        value = confirmPass,
                        onValueChange = { confirmPass = it },
                        header = "Confirm Password",
                        onValidityChange = { validConfirm = it },
                        onDone = { doSubmit() }
                    )
                }

                if (!isMatch) {
                    Column {
                        Text(
                            text = "Passwords do not match",
                            style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Medium, fontSize = 14.sp),
                            color = Color.Red
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CancelButton(onClick = { onDismiss(false) })

                    Spacer(modifier = Modifier.width(12.dp))

                    ConfirmButton(
                        text = "Create Account",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = valid && isMatch,
                        onClick = { doSubmit() }
                    )
                }
            }
        }
    }
}