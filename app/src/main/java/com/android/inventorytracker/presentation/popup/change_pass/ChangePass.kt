package com.android.inventorytracker.presentation.popup.change_pass

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.settings.viewmodel.AuthViewModel
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans
import kotlinx.coroutines.launch

@Composable
fun ChangePass(
    role: UserRole,
    onDismiss: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
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
                val success = authViewModel.updateUser(newPassword, if (role == UserRole.ADMIN) "ADMIN" else "STAFF")
                successChange = success
                if (success) onDismiss()
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.width(460.dp).wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            color = Palette.PopupSurface,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = if (role == UserRole.ADMIN) "Admin Password Change" else "Staff Password Change",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Palette.DarkBeigeText
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    PasswordField(
                        fieldModifier = Modifier.focusRequester(focusNewPass),
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        header = "New Password",
                        onValidityChange = { validNewPass = it },
                        onDone = { focusConfirmPass.requestFocus() }
                    )

                    PasswordField(
                        fieldModifier = Modifier.focusRequester(focusConfirmPass),
                        value = confirmPass,
                        onValueChange = { confirmPass = it },
                        header = "Confirm Password",
                        onValidityChange = { validConfirmPass = it },
                        onDone = { doSubmit() }
                    )
                }

                if (!isMatch || successChange == false) {
                    Column {
                        if (!isMatch) {
                            Text(
                                text = "Passwords do not match",
                                style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Medium, fontSize = 14.sp),
                                color = Color.Red
                            )
                        }
                        if (successChange == false) {
                            Text(
                                text = "Unable to change password",
                                style = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Medium, fontSize = 14.sp),
                                color = Color.Red
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CancelButton(onClick = onDismiss)
                    Spacer(modifier = Modifier.width(12.dp))
                    ConfirmButton(
                        text = "Update Password",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = valid && isMatch,
                        onClick = { doSubmit() }
                    )
                }
            }
        }
    }
}