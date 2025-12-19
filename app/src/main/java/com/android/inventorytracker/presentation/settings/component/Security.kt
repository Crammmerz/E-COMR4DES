package com.android.inventorytracker.presentation.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.R
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.popup.change_pass_admin.ChangePassAdmin
import com.android.inventorytracker.presentation.popup.change_pass_staff.ChangePassStaff
import com.android.inventorytracker.presentation.settings.viewmodel.SettingsViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.Sand
import com.android.inventorytracker.ui.theme.LightSand

// --- Define Google Sans Family ---
val GoogleSansFamily = FontFamily.Default

@Composable
fun Security(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isAuthEnabled by viewModel.authEnabled.collectAsState()
    val isRoleAuthEnabled by viewModel.roleAuthEnabled.collectAsState()
    var role by remember { mutableStateOf<UserRole?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Palette.MainBackground)
            .padding(horizontal = 40.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // --- HEADING ---
        Text(
            text = "User Account & Security",
            style = TextStyle(
                fontFamily = GoogleSansFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Palette.DarkBeigeText,
                letterSpacing = (-0.5).sp
            )
        )

        // --- Section 1: Authentication Settings ---
        SecuritySectionCard {
            // Enable Authentication Row
            SecurityRowItem(
                label = "Enable Authentication",
                fontFamily = GoogleSansFamily
            ) {
                Checkbox(
                    checked = isAuthEnabled,
                    onCheckedChange = { viewModel.toggleAuth(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Palette.ButtonBeigeBase,
                        uncheckedColor = LightSand,
                        checkmarkColor = Palette.DarkBrown
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Role Authentication Row
            SecurityRowItem(
                label = "Role Authentication",
                fontFamily = GoogleSansFamily
            ) {
                Checkbox(
                    checked = isRoleAuthEnabled,
                    onCheckedChange = { viewModel.toggleRoleAuth(it) },
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
            // Admin Password Row
            SecurityRowItem(
                label = "Admin Password Change",
                fontFamily = GoogleSansFamily
            ) {
                SecurityActionButton(
                    label = "Change Password",
                    enabled = isAuthEnabled,
                    fontFamily = GoogleSansFamily,
                    onClick = { role = UserRole.ADMIN }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Staff Password Row
            SecurityRowItem(
                label = "Staff Password Change",
                fontFamily = GoogleSansFamily
            ) {
                SecurityActionButton(
                    label = "Change Password",
                    enabled = isAuthEnabled && isRoleAuthEnabled,
                    fontFamily = GoogleSansFamily,
                    onClick = { role = UserRole.STAFF }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }

    // Popups
    when (role) {
        UserRole.ADMIN -> ChangePassAdmin(onDismiss = { role = null }, onSubmit = viewModel::updateUser)
        UserRole.STAFF -> ChangePassStaff(onDismiss = { role = null }, onSubmit = viewModel::updateUserStaff)
        else -> {}
    }
}

/**
 * Reusable Card for Sections
 */
@Composable
private fun SecuritySectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp))
            .background(Palette.PureWhite, shape = RoundedCornerShape(24.dp))
            .padding(24.dp),
        content = content
    )
}

/**
 * Reusable Row for items
 */
@Composable
private fun SecurityRowItem(
    label: String,
    fontFamily: FontFamily,
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
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = Palette.DarkBeigeText
            )
        )
        action()
    }
}

/**
 * Pill-shaped action button for Security Settings
 */
@Composable
private fun SecurityActionButton(
    label: String,
    enabled: Boolean,
    fontFamily: FontFamily,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(28.dp), // Pill-shaped
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
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        )
    }
}