package com.android.inventorytracker.presentation.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.popup.change_pass_admin.ChangePassAdmin
import com.android.inventorytracker.presentation.popup.change_pass_staff.ChangePassStaff
import com.android.inventorytracker.presentation.settings.viewmodel.SettingsViewModel
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.Sand
import com.android.inventorytracker.ui.theme.LightSand
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow // Import for shadow effect

@Composable
fun Security(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isAuthEnabled by viewModel.authEnabled.collectAsState()
    val isRoleAuthEnabled by viewModel.roleAuthEnabled.collectAsState()
    var role by remember { mutableStateOf<UserRole?>(null) }

    // 1. Maximized Background: Use Palette.MainBackground for the entire screen,
    // which simulates the white area of the setting tab itself.
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Palette.MainBackground) // Use MainBackground as the primary background
            .padding(horizontal = 40.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Banner / title row
        Text(
            text = "User Account & Security",
            style = MaterialTheme.typography.titleLarge,
            color = Palette.DarkBeigeText
        )

        // 2 & 3. Removed INNER Surface, contents are now directly in the outer Column.
        // We will apply the shadow/division to the content rows, usually grouped in sections.

        // --- Section 1: Authentication Settings ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp, // Added shadow for division
                    shape = RoundedCornerShape(12.dp) // Added rounded corners
                )
                .background(Palette.MainBackground) // Background of the section
                .padding(vertical = 16.dp, horizontal = 24.dp) // Padding for inner content
        ) {

            // Row 1: Enable Authentication
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Enable Authentication",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Palette.DarkBeigeText,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = isAuthEnabled,
                    onCheckedChange = { checked -> viewModel.toggleAuth(checked) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Palette.ButtonBeigeBase,
                        uncheckedColor = LightSand,
                        checkmarkColor = Palette.DarkBrown,
                        disabledCheckedColor = Sand,
                        disabledUncheckedColor = LightSand
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Spacer between items in the section

            // Row 2: Role Authentication
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Role Authentication",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Palette.DarkBeigeText,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = isRoleAuthEnabled,
                    onCheckedChange = { checked -> viewModel.toggleRoleAuth(checked) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Palette.ButtonBeigeBase,
                        uncheckedColor = LightSand,
                        checkmarkColor = Palette.DarkBrown,
                        disabledCheckedColor = Sand,
                        disabledUncheckedColor = LightSand
                    )
                )
            }
        } // End of Section 1

        Spacer(modifier = Modifier.height(8.dp)) // Spacer between sections

        // --- Section 2: Password Change ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp, // Added shadow for division
                    shape = RoundedCornerShape(12.dp) // Added rounded corners
                )
                .background(Palette.MainBackground) // Background of the section
                .padding(vertical = 16.dp, horizontal = 24.dp) // Padding for inner content
        ) {

            // Row 3: Admin Password Change
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Admin Password Change",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Palette.DarkBeigeText,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { role = UserRole.ADMIN },
                    enabled = isAuthEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Palette.ButtonBeigeBase,
                        contentColor = Palette.ButtonDarkBrown,
                        disabledContainerColor = Sand,
                        disabledContentColor = Palette.DarkBeigeText
                    )
                ) {
                    Text(text = "Change Password")
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Spacer between items in the section

            // Row 4: Staff Password Change
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Staff Password Change",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Palette.DarkBeigeText,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { role = UserRole.STAFF },
                    enabled = isAuthEnabled && isRoleAuthEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Palette.ButtonBeigeBase,
                        contentColor = Palette.ButtonDarkBrown,
                        disabledContainerColor = Sand,
                        disabledContentColor = Palette.DarkBeigeText
                    )
                ) {
                    Text(text = "Change Password")
                }
            }
        } // End of Section 2

        // Optional: Add a Spacer to push content to the top (if needed)
        Spacer(modifier = Modifier.weight(1f))
    }

    // Popups remain outside the main Column
    when (role) {
        UserRole.ADMIN ->
            ChangePassAdmin(
                onDismiss = { role = null },
                onSubmit = viewModel::updateUser
            )
        UserRole.STAFF ->
            ChangePassStaff(
                onDismiss = { role = null },
                onSubmit = viewModel::updateUserStaff
            )
        else -> {}
    }
}