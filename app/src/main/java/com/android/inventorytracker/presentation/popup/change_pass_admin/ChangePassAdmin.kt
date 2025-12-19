package com.android.inventorytracker.presentation.popup.change_pass_admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.* // Import para sa Surface at LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.android.inventorytracker.R
import com.android.inventorytracker.data.local.entities.UserEntity
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.Palette
import kotlinx.coroutines.launch

private val GoogleSansFamily = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_medium, FontWeight.Medium),
    Font(R.font.google_sans_semibold, FontWeight.SemiBold)
)

@Composable
fun ChangePassAdmin(
    onDismiss: () -> Unit,
    onSubmit: suspend (UserEntity, newPassword: String) -> Boolean
) {
    var oldPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPass by rememberSaveable { mutableStateOf("") }

    // State para sa validation para mawala ang "No value passed" errors
    var validOld by remember { mutableStateOf(false) }
    var validNew by remember { mutableStateOf(false) }
    var validConfirm by remember { mutableStateOf(false) }

    val valid = validOld && validNew && validConfirm
    val isMatch = newPassword == confirmPass
    val scope = rememberCoroutineScope()

    val focusOldPass = remember { FocusRequester() }
    val focusNewPass = remember { FocusRequester() }
    val focusConfirmPass = remember { FocusRequester() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.width(460.dp).wrapContentHeight(),
            shape = RoundedCornerShape(32.dp),
            color = Palette.iOSCardWhite,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Admin Password Change",
                    style = TextStyle(
                        fontFamily = GoogleSansFamily,
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
                        onValidityChange = { validOld = it } // Fix para sa error
                    )
                    PasswordField(
                        fieldModifier = Modifier.focusRequester(focusNewPass),
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        header = "New Password",
                        onValidityChange = { validNew = it } // Fix para sa error
                    )
                    PasswordField(
                        fieldModifier = Modifier.focusRequester(focusConfirmPass),
                        value = confirmPass,
                        onValueChange = { confirmPass = it },
                        header = "Confirm Password",
                        onValidityChange = { validConfirm = it } // Fix para sa error
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ito ang "teknik" para maging Google Sans ang buttons
                    CompositionLocalProvider(
                        LocalTextStyle provides TextStyle(
                            fontFamily = GoogleSansFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    ) {
                        CancelButton(onClick = onDismiss)

                        Spacer(modifier = Modifier.width(12.dp))

                        ConfirmButton(
                            text = "Change Password",
                            enabled = valid && isMatch,
                            onClick = {
                                scope.launch {
                                    val user = UserEntity(username = "admin", passwordHash = oldPassword, role = "ADMIN")
                                    if(onSubmit(user, newPassword)) onDismiss()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}