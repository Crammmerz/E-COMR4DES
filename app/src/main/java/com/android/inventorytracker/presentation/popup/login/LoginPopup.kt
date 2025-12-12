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
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.UserEntity
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import kotlinx.coroutines.launch
import com.android.inventorytracker.R

// --- Pure White & Beige Palette (Uniform) ---
private val PureWhite = Color(0xFFFFFFFF)           // Card background
private val BorderLightBeige = Color(0xFFDDDCDA)    // Light border color (iOS-like divider)
private val DarkBeigeText = Color(0xFF523F31)        // Dark text (Headers, primary contrast)
private val LightBeigeText = Color(0xFF796254)       // Medium text color (Secondary actions)
private val AccentBeigePrimary = Color(0xFFB08959)   // Primary action color (Deep Beige)

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

    LaunchedEffect(Unit) {
        focusUsername.requestFocus()
    }

    fun onSubmit(){
        if(valid){
            val user = UserEntity(username = username, passwordHash = password, role = userRole.name)
            scope.launch {
                val success = onLogin(user)
                loginSuccess = success
                if(success) onDismiss()
            }
        }
    }
    Dialog(onDismissRequest = onDismiss) {
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
                        .background(PureWhite) // Use Pure White background
                        .border(
                            width = 1.dp,
                            color = BorderLightBeige, // Use light beige border
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
                            color = DarkBeigeText // Use Dark Beige Text
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
                        // Assuming StringField and PasswordField use the global theme/GoogleSans.
                        // If they are local, their TextStyle needs to be updated too.
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
                        onDone = { onSubmit() }
                    )

                    if(loginSuccess == false){
                        Text("Invalid user credential") //TODO: UI DESIGN
                    }

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
                            onSubmit()
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
            // Use AccentBeigePrimary for primary action, dimmed when disabled
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
            contentColor = LightBeigeText // Use Light Beige for secondary/cancel action
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