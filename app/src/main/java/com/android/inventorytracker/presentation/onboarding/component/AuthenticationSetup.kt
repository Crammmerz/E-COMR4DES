package com.android.inventorytracker.presentation.onboarding.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.settings.component.SecurityRowItem
import com.android.inventorytracker.presentation.settings.viewmodel.AuthViewModel
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette
import kotlinx.coroutines.launch

@Composable
fun AuthenticationSetup(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val isAuthEnabled by viewModel.authEnabled.collectAsState()
    val isRoleAuthEnabled by viewModel.roleAuthEnabled.collectAsState()

    var showCreateAdminAcc by remember { mutableStateOf(false) }
    var showCreateStaffAcc by remember { mutableStateOf(false) }

    val adminCount by viewModel.getCount("ADMIN").collectAsState(initial = 0)
    val staffCount by viewModel.getCount("STAFF").collectAsState(initial = 0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 120.dp, vertical = 60.dp)
    ) {

        // HEADER
        Text(
            text = "StockWise",
            modifier = Modifier.align(Alignment.TopStart),
            style = TextStyle(
                fontFamily = GoogleSans,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Palette.DarkBeigeText,
                letterSpacing = (-2.5).sp
            )
        )

        // MAIN LAYER
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp)
        ) {

            // ================= LEFT FIXED SECURITY PANEL =================
            Surface(
                modifier = Modifier
                    .width(420.dp)
                    .align(Alignment.TopStart)
                    .zIndex(1f),
                color = Palette.iOSCardWhite,
                shape = RoundedCornerShape(32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "SECURITY SETUP",
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Palette.ButtonDarkBrown.copy(alpha = 0.5f),
                            letterSpacing = 1.5.sp
                        )
                    )

                    SecurityRowItem(label = "Enable Authentication") {
                        Switch(
                            checked = isAuthEnabled,
                            onCheckedChange = { checked ->
                                if (adminCount == 0 && checked) {
                                    showCreateAdminAcc = true
                                } else {
                                    viewModel.toggleAuth(checked)
                                }
                            }
                        )
                    }

                    SecurityRowItem(label = "Role Authentication") {
                        Switch(
                            checked = isRoleAuthEnabled,
                            enabled = isAuthEnabled,
                            onCheckedChange = { checked ->
                                if (staffCount == 0 && checked) {
                                    showCreateStaffAcc = true
                                } else {
                                    viewModel.toggleRoleAuth(checked)
                                }
                            }
                        )
                    }
                }
            }

            // ================= RIGHT CONTENT / POPUP =================
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 460.dp)
                    .zIndex(2f),
                contentAlignment = Alignment.TopCenter
            ) {
                when {
                    showCreateAdminAcc -> {
                        CreateAccount(
                            role = "ADMIN",
                            onDismiss = { success ->
                                if (success) viewModel.toggleAuth(true)
                                showCreateAdminAcc = false
                            },
                            onSubmit = viewModel::register
                        )
                    }

                    showCreateStaffAcc -> {
                        CreateAccount(
                            role = "STAFF",
                            onDismiss = { success ->
                                if (success) viewModel.toggleRoleAuth(true)
                                showCreateStaffAcc = false
                            },
                            onSubmit = viewModel::register
                        )
                    }

                    else -> {
                        Column(
                            modifier = Modifier.padding(top = 120.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Palette.DarkBeigeText.copy(alpha = 0.15f),
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Enable authentication to add\nAdmin or Staff users.",
                                style = TextStyle(
                                    fontFamily = GoogleSans,
                                    fontSize = 18.sp,
                                    color = Palette.DarkBeigeText.copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateAccount(
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

    val scope = rememberCoroutineScope()
    val focusUsername = remember { FocusRequester() }
    val focusPassword = remember { FocusRequester() }
    val focusConfirmPass = remember { FocusRequester() }

    val isMatch = password == confirmPass

    fun submit() {
        scope.launch {
            onSubmit(username, password, role)
            onDismiss(true)
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = Palette.PopupSurface,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = if (role == "ADMIN") "Create Admin Account" else "Create Staff Account",
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Palette.DarkBeigeText
                )
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
                    onDone = {
                        if (validUsername && validPassword && validConfirm && isMatch) submit()
                    }
                )
            }

            if (!isMatch && confirmPass.isNotEmpty()) {
                Text(
                    text = "Passwords do not match",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontSize = 14.sp,
                        color = Color.Red
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                CancelButton(onClick = { onDismiss(false) })
                Spacer(modifier = Modifier.width(16.dp))
                ConfirmButton(
                    text = "Create Account",
                    containerColor = Palette.ButtonDarkBrown,
                    enabled = validUsername && validPassword && validConfirm && isMatch,
                    onClick = { submit() }
                )
            }
        }
    }
}
