package com.android.inventorytracker.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.LoginState
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.popup.login.LoginPopup
import com.android.inventorytracker.R

// --- Pure White & Beige Palette ---
private val PureWhite = Color(0xFFFFFFFF)        // Strict white background
private val DarkBeigeText = Color(0xFF523F31)    // Dark text (Primary contrast)
private val LightBeigeText = Color(0xFF796254)   // Lighter text (Secondary contrast)
private val ButtonBeigeBase = Color(0xFFD3B386)  // Main beige for buttons (Lighter shade)
private val ButtonBeigeEnd = Color(0xFFB08959)   // Deeper beige for gradient end
private val ButtonBeigeHover = Color(0xFFE7D0AC) // Very light beige for hover feedback

// Google Sans / Product Sans font family (Retained)
private val GoogleSans = FontFamily(
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
    val header = if (isRoleAuthEnabled) "Login Type" else "Login"
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite), // Apply strict white background
        color = PureWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 64.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Lumi Cafe",
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 80.sp,
                    color = DarkBeigeText // Use dark beige for main title
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = header,
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 50.sp,
                    color = LightBeigeText // Use lighter beige for sub-title
                )
            )

            Spacer(modifier = Modifier.height(70.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.7f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoginButton(
                    label = "Admin Login",
                    onClick = {
                        loginViewModel.updateUserRole(UserRole.ADMIN)
                        showDialog = true
                    }
                )

                if (isRoleAuthEnabled) {
                    Spacer(modifier = Modifier.width(24.dp))

                    LoginButton(
                        label = "Staff Login",
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

@Composable
fun LoginButton(
    label: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // Use a vertical gradient entirely within the beige shade
    val backgroundBrush = Brush.verticalGradient(
        colors = if (isHovered) {
            // Hover state is slightly lighter beige
            listOf(ButtonBeigeHover, ButtonBeigeBase)
        } else {
            // Default state is a gradient from lighter to deeper beige
            listOf(ButtonBeigeBase, ButtonBeigeEnd)
        }
    )

    Button(
        modifier = Modifier
            .widthIn(min = 260.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(backgroundBrush)
            .hoverable(interactionSource = interactionSource),
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // Transparent since background is applied via brush
            contentColor = PureWhite // White text inside the beige button
        ),
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = PureWhite // White text
            )
        )
    }
}