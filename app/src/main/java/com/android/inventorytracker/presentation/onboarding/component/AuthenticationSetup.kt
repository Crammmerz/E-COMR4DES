package com.android.inventorytracker.presentation.onboarding.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.popup.create_acc.CreateAccPopup
import com.android.inventorytracker.presentation.settings.component.SecurityActionButton
import com.android.inventorytracker.presentation.settings.component.SecurityRowItem
import com.android.inventorytracker.presentation.settings.component.SecuritySectionCard
import com.android.inventorytracker.presentation.settings.viewmodel.AuthViewModel
import com.android.inventorytracker.presentation.shared.component.input_fields.PasswordField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.LightSand
import com.android.inventorytracker.ui.theme.Palette
import kotlinx.coroutines.launch

@Composable
fun AuthenticationSetup(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val backgroundColor = Color(0xFFFEF7ED)

    val isAuthEnabled by viewModel.authEnabled.collectAsState()
    val isRoleAuthEnabled by viewModel.roleAuthEnabled.collectAsState()

    var showCreateAdminAcc by remember { mutableStateOf(false) }
    var showCreateStaffAcc by remember { mutableStateOf(false) }

    val adminCount by viewModel.getCount("ADMIN").collectAsState(initial = 0)
    val staffCount by viewModel.getCount("STAFF").collectAsState(initial = 0)

    Row {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            color = backgroundColor
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp, vertical = 32.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // --- HEADER ---
                    Text(
                        text = "User Account & Security",
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 34.sp,
                            color = Palette.DarkBeigeText
                        )
                    )

                    // Content Wrapper para sa Cards
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // --- Section 1: Authentication Settings ---
                        SecuritySectionCard {
                            SecurityRowItem(label = "Enable Authentication") {
                                Checkbox(
                                    checked = isAuthEnabled,
                                    onCheckedChange = { it ->
                                        if (adminCount == 0) {
                                            showCreateAdminAcc = true
                                            viewModel.toggleAuth(false)
                                        } else {
                                            viewModel.toggleAuth(it)
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Palette.ButtonBeigeBase,
                                        uncheckedColor = LightSand,
                                        checkmarkColor = Palette.DarkBrown
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            SecurityRowItem(label = "Role Authentication") {
                                Checkbox(
                                    checked = isRoleAuthEnabled,
                                    enabled = isAuthEnabled,
                                    onCheckedChange = { it ->
                                        if (staffCount == 0) {
                                            showCreateStaffAcc = true
                                            viewModel.toggleRoleAuth(false)
                                        } else {
                                            viewModel.toggleRoleAuth(it)
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Palette.ButtonBeigeBase,
                                        uncheckedColor = LightSand,
                                        checkmarkColor = Palette.DarkBrown
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        Column (modifier = Modifier.weight(1f)) {
            when {
                showCreateAdminAcc -> {
                    CreateAccount(
                        role = "ADMIN",
                        onDismiss = {
                            if (it) viewModel.toggleAuth(true)
                            showCreateAdminAcc = false
                        },
                        onSubmit = viewModel::register
                    )
                }

                showCreateStaffAcc -> {
                    CreateAccount(
                        role = "STAFF",
                        onDismiss = {
                            if (it) viewModel.toggleRoleAuth(true)
                            showCreateStaffAcc = false
                        },
                        onSubmit = viewModel::register
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Authentication Icon",
                            tint = Palette.DarkBrown,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No accounts created yet.\nEnable authentication to add Admin or Staff users.",
                            style = TextStyle(
                                fontFamily = GoogleSans,
                                fontSize = 16.sp,
                                color = Palette.DarkBeigeText,
                                textAlign = TextAlign.Center
                            )
                        )
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

    Surface(
        modifier = Modifier.fillMaxSize(),
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