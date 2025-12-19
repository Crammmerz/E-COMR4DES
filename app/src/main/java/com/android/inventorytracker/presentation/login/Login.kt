package com.android.inventorytracker.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.popup.login.LoginPopup
import com.android.inventorytracker.R
import com.android.inventorytracker.ui.theme.Palette

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
    val isRoleAuthEnabled = loginViewModel.roleAuthEnabled
    val header = if (isRoleAuthEnabled) "Select Login Type" else "Log In"
    var showDialog by remember { mutableStateOf(false) }

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
            Text(
                text = "Inventory Tracker",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 64.sp,
                    fontFamily = GoogleSansFamily,
                    color = Palette.DarkBeigeText,
                    letterSpacing = (-1.5).sp // iOS-style tracking
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = header,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    fontFamily = GoogleSansFamily,
                    color = Palette.DarkBeigeText.copy(alpha = 0.7f)
                )
            )

            Spacer(modifier = Modifier.height(80.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.6f), // Mas slim na width para sa buttons
                horizontalArrangement = Arrangement.spacedBy(20.dp), // Modern spacing
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoginRefinedButton(
                    label = "Admin Login",
                    containerColor = Palette.ButtonBeigeBase,
                    contentColor = Palette.ButtonDarkBrown,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        loginViewModel.updateUserRole(UserRole.ADMIN)
                        showDialog = true
                    }
                )

                if (isRoleAuthEnabled) {
                    LoginRefinedButton(
                        label = "Staff Login",
                        containerColor = Color(0xFFE5E5EA), // iOS Light Gray feel
                        contentColor = Palette.DarkBeigeText,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            loginViewModel.updateUserRole(UserRole.STAFF)
                            showDialog = true
                        }
                    )
                }
            }

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
 * iOS-INSPIRED BUTTON DESIGN:
 * - Full rounding (Pill-shaped)
 * - Soft colors, no heavy shadows
 * - Google Sans Semibold for clean look
 */
@Composable
fun LoginRefinedButton(
    label: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(56.dp), // Standard iOS button height
        onClick = onClick,
        shape = RoundedCornerShape(28.dp), // Fully rounded / Pill-shaped
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp // Konting depth lang pag clinick
        )
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                fontFamily = GoogleSansFamily,
                letterSpacing = 0.sp
            )
        )
    }
}