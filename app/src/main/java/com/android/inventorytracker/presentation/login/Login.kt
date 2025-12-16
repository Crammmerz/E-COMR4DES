package com.android.inventorytracker.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.popup.login.LoginPopup
import com.android.inventorytracker.R
import com.android.inventorytracker.ui.theme.Palette

// --- Define Google Sans Font Family (Assuming no Bold weight is available) ---
// NOTE: Ensure these resource names (R.font.google_sans_...) match your actual font files.
val GoogleSansFamily = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_medium, FontWeight.Medium),
    Font(R.font.google_sans_semibold, FontWeight.SemiBold)
)

@Composable
fun Login(
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val userRole = loginViewModel.userRole

    // Kept the original state access for isRoleAuthEnabled as requested
    val isRoleAuthEnabled = loginViewModel.roleAuthEnabled

    val header = if (isRoleAuthEnabled) "Select Login Type" else "Log In"
    var showDialog by remember { mutableStateOf(false) }

    // Outer Surface: Maximize white background
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Palette.PureWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title: Inventory Tracker
            Text(
                text = "Inventory Tracker",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 64.sp,
                    fontFamily = GoogleSansFamily,
                    color = Palette.DarkBeigeText
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Subtitle
            Text(
                text = header,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp,
                    fontFamily = GoogleSansFamily,
                    color = Palette.DarkBeigeText
                )
            )

            Spacer(modifier = Modifier.height(60.dp))

            // --- Horizontal Button Alignment ---
            Row(
                modifier = Modifier.fillMaxWidth(0.7f), // Keep the button group centered and proportional
                horizontalArrangement = Arrangement.Center, // Center the buttons
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Admin Button
                LoginRefinedButton(
                    label = "Admin Login",
                    onClick = {
                        loginViewModel.updateUserRole(UserRole.ADMIN)
                        showDialog = true
                    },
                    modifier = Modifier.weight(1f) // Makes button fill available space
                )

                if (isRoleAuthEnabled) {
                    Spacer(modifier = Modifier.width(24.dp)) // Spacer between buttons

                    // Staff Button
                    LoginRefinedButton(
                        label = "Staff Login",
                        onClick = {
                            loginViewModel.updateUserRole(UserRole.STAFF)
                            showDialog = true
                        },
                        modifier = Modifier.weight(1f) // Makes button fill available space
                    )
                }
            }

            // Login Popup
            if (showDialog) {
                LoginPopup(
                    userRole = userRole,
                    onDismiss = { showDialog = false },
                    onLogin = loginViewModel::onLogin
                )
            }
        }
    }
}

/**
 * IMPROVED BUTTON DESIGN: Wide, rounded, and uses a subtle shadow for a modern, raised look.
 */
@Composable
fun LoginRefinedButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .height(60.dp) // Slightly taller button
            .shadow(
                elevation = 6.dp, // Subtle shadow for depth
                shape = RoundedCornerShape(16.dp)
            ),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp), // Rounded corners
        colors = ButtonDefaults.buttonColors(
            // Use a solid beige color
            containerColor = Palette.ButtonBeigeBase,
            contentColor = Palette.PureWhite
        ),
        // Use 0 elevation in ButtonDefaults since we are applying elevation via .shadow() modifier
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp, // Slightly larger font for prominence
                fontFamily = GoogleSansFamily
            )
        )
    }
}