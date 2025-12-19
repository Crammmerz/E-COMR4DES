package com.android.inventorytracker.presentation.popup.change_pass_staff

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

// --- Define Google Sans Family ---
private val GoogleSansFamily = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_medium, FontWeight.Medium),
    Font(R.font.google_sans_semibold, FontWeight.SemiBold)
)

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
                val success = onSubmit(
                    UserEntity(
                        username = "staff",
                        passwordHash = newPassword,
                        role = "STAFF"
                    )
                )
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
            modifier = Modifier
                .width(460.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(32.dp),
            color = Palette.iOSCardWhite,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header
                Text(
                    text = "Staff Password Change",
                    style = TextStyle(
                        fontFamily = GoogleSansFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
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
                        onDone = { if (isMatch) focusConfirmPass.requestFocus() }
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

                // Error Messages
                Column {
                    if (!isMatch) {
                        Text(
                            text = "Passwords do not match",
                            style = TextStyle(
                                fontFamily = GoogleSansFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            ),
                            color = Color.Red
                        )
                    }
                    if (successChange == false) {
                        Text(
                            text = "Unable to change password",
                            style = TextStyle(
                                fontFamily = GoogleSansFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            ),
                            color = Color.Red
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Force Google Sans on Buttons
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
                            onClick = { doSubmit() }
                        )
                    }
                }
            }
        }
    }
}