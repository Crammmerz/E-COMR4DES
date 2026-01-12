package com.android.inventorytracker.presentation.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.popup.change_pass_admin.ChangePassAdmin
import com.android.inventorytracker.presentation.popup.change_pass.ChangePass
import com.android.inventorytracker.presentation.popup.create_acc.CreateAccPopup
import com.android.inventorytracker.presentation.settings.viewmodel.AuthViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.Sand
import com.android.inventorytracker.ui.theme.LightSand
import com.android.inventorytracker.ui.theme.GoogleSans

@Composable
fun Security(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val backgroundColor = Color(0xFFFEF7ED)

    val isAuthEnabled by viewModel.authEnabled.collectAsState()
    val isRoleAuthEnabled by viewModel.roleAuthEnabled.collectAsState()

    var showCreateAdminAcc by remember { mutableStateOf(false) }
    var showCreateStaffAcc by remember { mutableStateOf(false) }

    var showAdminPasswordChange by remember { mutableStateOf(false) }
    var showStaffPasswordChange by remember { mutableStateOf(false) }

    val adminCount by viewModel.getCount("ADMIN").collectAsState(initial = 0)
    val staffCount by viewModel.getCount("STAFF").collectAsState(initial = 0)

    Surface(
        modifier = Modifier.fillMaxSize(),
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
                                    if(adminCount == 0){
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
                                onCheckedChange = { it ->
                                    if(isAuthEnabled) {
                                        if(staffCount == 0){
                                            showCreateStaffAcc = true
                                            viewModel.toggleRoleAuth(false)
                                        } else {
                                            viewModel.toggleRoleAuth(it)
                                        }
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

                    // --- Section 2: Password Change ---
                    SecuritySectionCard {
                        SecurityRowItem(label = "Admin Password Change") {
                            SecurityActionButton(
                                label = "Change Password",
                                enabled = isAuthEnabled,
                                onClick = { showAdminPasswordChange = true }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        SecurityRowItem(label = "Staff Password Change") {
                            SecurityActionButton(
                                label = "Change Password",
                                enabled = isAuthEnabled && isRoleAuthEnabled,
                                onClick = { showStaffPasswordChange = true }
                            )
                        }
                    }
                }
            }
        }
    }

    if(showCreateAdminAcc) {
        CreateAccPopup(
            role = "ADMIN",
            onDismiss = {
                if (it) viewModel.toggleAuth(true)
                showCreateAdminAcc = false
            },
            onSubmit = viewModel::register
        )
    }

    if(showCreateStaffAcc){
        CreateAccPopup(
            role = "STAFF",
            onDismiss = {
                if (it) viewModel.toggleRoleAuth(true)
                showCreateStaffAcc = false
            },
            onSubmit = viewModel::register
        )
    }

    if(showAdminPasswordChange) ChangePassAdmin(onDismiss = { showAdminPasswordChange = false }, onSubmit = viewModel::updateUser)
    if(showStaffPasswordChange) ChangePass(role = UserRole.STAFF, onDismiss = { showStaffPasswordChange = false })
}

@Composable
fun SecuritySectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp))
            .background(Palette.PureWhite, shape = RoundedCornerShape(24.dp))
            .padding(24.dp),
        content = content
    )
}

@Composable
fun SecurityRowItem(
    label: String,
    action: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = Palette.DarkBeigeText
            )
        )
        action()
    }
}

@Composable
fun SecurityActionButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Palette.ButtonBeigeBase,
            contentColor = Palette.ButtonDarkBrown,
            disabledContainerColor = Sand,
            disabledContentColor = Palette.DarkBeigeText.copy(alpha = 0.5f)
        ),
        modifier = Modifier.height(48.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        )
    }
}