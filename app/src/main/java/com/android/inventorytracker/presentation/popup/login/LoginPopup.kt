package com.android.inventorytracker.presentation.popup.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.R

// Card + text colors
private val LumiCardBg = Color(0xFFFFFFFF)
private val LumiCardBorder = Color(0xFFE7E4DD)
private val LumiTextDarkBeige = Color(0xFF523F31)
private val LumiTextBlack = Color(0xFF111111)
private val BeigeBorder = Color(0xFFC79F6D)

// Google Sans
private val GoogleSans = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_medium, FontWeight.Medium),
    Font(R.font.google_sans_semibold, FontWeight.SemiBold)
)

@Composable
fun LoginPopup(
    userRole: UserRole,
    onDismiss: () -> Unit,
    onLogin: (username: String, password: String, userRole: String) -> Unit,
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var validUsername by rememberSaveable { mutableStateOf(false) }
    var validPassword by rememberSaveable { mutableStateOf(false) }

    val focusUsername = remember { FocusRequester() }
    val focusPassword = remember { FocusRequester() }

    val header = when (userRole) {
        UserRole.ADMIN -> "Admin Login"
        UserRole.STAFF -> "Staff Login"
    }

    Dialog(onDismissRequest = onDismiss) {
        // Walang overlay, same background framing as main screen
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(LumiCardBg)
                        .border(
                            width = 1.dp,
                            color = LumiCardBorder,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    // Header
                    Text(
                        text = header,
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                            color = LumiTextDarkBeige
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Username
                    StringField(
                        value = username,
                        onValueChange = { username = it },
                        header = "Username",
                        placeholder = "Enter username",
                        modifier = Modifier.fillMaxWidth(),
                        maxLength = 99,
                        inputModifier = Modifier.focusRequester(focusUsername),
                        onValidationChange = { validUsername = it },
                        onDone = {
                            focusPassword.requestFocus()
                        },
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password
                    PasswordField(
                        value = password,
                        onValueChange = { password = it },
                        header = "Password",
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusPassword),
                        onValidityChange = { validPassword = it },
                        onDone = {
                            if (validUsername && validPassword) {
                                onLogin(username, password, userRole.name)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Actions (right aligned)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButtonSecondary(
                            label = "Cancel",
                            onClick = onDismiss
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        LoginTextAction(
                            enabled = validUsername && validPassword
                        ) {
                            if (validUsername && validPassword) {
                                onLogin(username, password, userRole.name)
                            }
                        }
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            focusUsername.requestFocus()
        }
    }
}

@Composable
private fun LoginTextAction(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = "Login",
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable(enabled = enabled, onClick = onClick),
        style = TextStyle(
            fontFamily = GoogleSans,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = if (enabled) BeigeBorder else BeigeBorder.copy(alpha = 0.4f)
        )
    )
}

@Composable
private fun TextButtonSecondary(
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        modifier = Modifier.height(44.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = LumiTextBlack
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        )
    }
}
