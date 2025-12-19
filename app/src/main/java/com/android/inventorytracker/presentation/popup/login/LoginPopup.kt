package com.android.inventorytracker.presentation.popup.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.android.inventorytracker.R
import com.android.inventorytracker.data.local.entities.UserEntity
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import kotlinx.coroutines.launch

// --- Define Google Sans Font Family ---
val GoogleSansFamily = FontFamily.Default

// --- Pure White & Beige Palette ---
private val PureWhite = Color(0xFFFFFFFF)
private val BorderLightBeige = Color(0xFFDDDCDA)
private val DarkBeigeText = Color(0xFF523F31)
private val LightBeigeText = Color(0xFF796254)
private val AccentBeigePrimary = Color(0xFFB08959)

@Composable
fun LoginPopup(
    userRole: UserRole,
    onDismiss: () -> Unit,
    onLogin: suspend (user: UserEntity) -> Boolean,
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var validUsername by rememberSaveable { mutableStateOf(false) }
    var validPassword by rememberSaveable { mutableStateOf(false) }
    var loginSuccess by rememberSaveable { mutableStateOf<Boolean?>(null) }

    val valid = validUsername && validPassword
    val scope = rememberCoroutineScope()
    val focusUsername = remember { FocusRequester() }
    val focusPassword = remember { FocusRequester() }

    val header = when (userRole) {
        UserRole.ADMIN -> "Admin Login"
        UserRole.STAFF -> "Staff Login"
    }

    Dialog(onDismissRequest = onDismiss) {
        // Ginagamit ang CompositionLocalProvider para piliting maging Google Sans
        // ang lahat ng text sa loob, pati ang labels sa StringField at PasswordField.
        CompositionLocalProvider(
            LocalTextStyle provides TextStyle(fontFamily = GoogleSansFamily)
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
                        .background(PureWhite)
                        .border(1.dp, BorderLightBeige, RoundedCornerShape(24.dp))
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = header,
                        style = TextStyle(
                            fontFamily = GoogleSansFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                            color = DarkBeigeText
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    StringField(
                        value = username,
                        onValueChange = { username = it },
                        header = "Username",
                        placeholder = "Enter username",
                        modifier = Modifier.fillMaxWidth(),
                        maxLength = 99,
                        inputModifier = Modifier.focusRequester(focusUsername),
                        onValidationChange = { validUsername = it },
                        onDone = { focusPassword.requestFocus() },
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PasswordField(
                        value = password,
                        onValueChange = { password = it },
                        header = "Password",
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusPassword),
                        onValidityChange = { validPassword = it },
                        onDone = {
                            if(valid) {
                                val user = UserEntity(username = username, passwordHash = password, role = userRole.name)
                                scope.launch {
                                    val success = onLogin(user)
                                    loginSuccess = success
                                    if(success) onDismiss()
                                }
                            }
                        }
                    )

                    if(loginSuccess == false){
                        Text(
                            text = "Invalid user credential",
                            style = TextStyle(
                                fontFamily = GoogleSansFamily,
                                color = Color.Red,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

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
                            enabled = valid
                        ) {
                            val user = UserEntity(username = username, passwordHash = password, role = userRole.name)
                            scope.launch {
                                val success = onLogin(user)
                                loginSuccess = success
                                if(success) onDismiss()
                            }
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
            fontFamily = GoogleSansFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = if (enabled) AccentBeigePrimary else AccentBeigePrimary.copy(alpha = 0.4f)
        )
    )
}

@Composable
private fun TextButtonSecondary(
    label: String = "Cancel",
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        modifier = Modifier.height(44.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = LightBeigeText
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = GoogleSansFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        )
    }
}