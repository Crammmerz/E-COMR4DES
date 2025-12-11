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
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.popup.login.LoginPopup
import com.android.inventorytracker.R

// Colors
private val LumiBackgroundWhite = Color(0xfffffbf4)
private val LumiTextDarkBeige = Color(0xFF523f31)
private val LumiTextBlack = Color(0xFF111111)

// Always‑on soft beige gradient (a bit deeper than background)
private val BeigeTop = Color(0xFF796254)      // light beige
private val BeigeBottom = Color(0xFF9d8a7c)   // warmer beige

// Hover state: slightly darker beige for feedback
private val BeigeHoverTop = Color(0xFFE7C89E)
private val BeigeHoverBottom = Color(0xFFD1A26E)
private val BeigeHoverBorder = Color(0xFFAA7C46)

// Google Sans / Product Sans font family (place fonts in res/font)
private val GoogleSans = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_medium, FontWeight.Medium),
    Font(R.font.google_sans_semibold, FontWeight.SemiBold)
)

@Composable
fun Login(
    userRole: UserRole,
    onSetUserRole: (UserRole) -> Unit,
    onLogin: (username: String, password: String, userRole: String) -> Unit,
    isRoleAuthEnabled: Boolean
) {
    val header = if (isRoleAuthEnabled) "Login Type" else "Login"
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(LumiBackgroundWhite),
        color = LumiBackgroundWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Lumi Cafe",
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 80.sp,
                    color = LumiTextDarkBeige
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = header,
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 50.sp,
                    color = LumiTextBlack
                )
            )

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoginButton(
                    label = "Admin Login",
                    onClick = {
                        onSetUserRole(UserRole.ADMIN)
                        showDialog = true
                    }
                )

                if (isRoleAuthEnabled) {
                    Spacer(modifier = Modifier.width(16.dp))

                    LoginButton(
                        label = "Staff Login",
                        onClick = {
                            onSetUserRole(UserRole.STAFF)
                            showDialog = true
                        }
                    )
                }
            }

            if (showDialog) {
                LoginPopup(
                    userRole = userRole,
                    onDismiss = { showDialog = false },
                    onLogin = onLogin
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

    val backgroundBrush = if (isHovered) {
        Brush.verticalGradient(
            colors = listOf(
                BeigeHoverTop,
                BeigeHoverBottom
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                BeigeTop,
                BeigeBottom
            )
        )
    }

    Button(
        modifier = Modifier
            .widthIn(min = 260.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(backgroundBrush)
            .hoverable(interactionSource = interactionSource),
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(999.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = LumiTextBlack
        ),
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = LumiBackgroundWhite
            )
        )
    }
}
