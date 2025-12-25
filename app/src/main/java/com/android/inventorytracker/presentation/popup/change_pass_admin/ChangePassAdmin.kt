package com.android.inventorytracker.presentation.popup.change_pass_admin

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
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans
import kotlinx.coroutines.launch

@Composable
fun ChangePassAdmin(
    onDismiss: () -> Unit,
    onSubmit: suspend (UserEntity, newPassword: String) -> Boolean
) {
    var oldPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPass by rememberSaveable { mutableStateOf("") }

    var validOld by remember { mutableStateOf(false) }
    var validNew by remember { mutableStateOf(false) }
    var validConfirm by remember { mutableStateOf(false) }

    val valid = validOld && validNew && validConfirm
    val isMatch = newPassword == confirmPass
    var successChange by rememberSaveable { mutableStateOf<Boolean?>(null) }

    val scope = rememberCoroutineScope()
    val focusOldPass = remember { FocusRequester() }
    val focusNewPass = remember { FocusRequester() }
    val focusConfirmPass = remember { FocusRequester() }

    fun doSubmit(){
        scope.launch {
            val user = UserEntity(username = "admin", passwordHash = oldPassword, role = "ADMIN")
            val success = onSubmit(user, newPassword)
            successChange = success
            if(success) onDismiss()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.width(460.dp).wrapContentHeight(),
            shape = RoundedCornerShape(20.dp), // Consistent with Item Removal
            color = Palette.PopupSurface, // Matches other popups
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Admin Password Change",
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
                        fieldModifier = Modifier.focusRequester(focusOldPass),
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        header = "Old Password",
                        onValidityChange = { validOld = it },
                        onDone = { focusNewPass.requestFocus() }
                    )
                    PasswordField(
                        fieldModifier = Modifier.focusRequester(focusNewPass),
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        header = "New Password",
                        onValidityChange = { validNew = it },
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