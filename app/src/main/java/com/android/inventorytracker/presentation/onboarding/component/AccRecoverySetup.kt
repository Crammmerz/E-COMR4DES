package com.android.inventorytracker.presentation.onboarding.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun AccRecoverySetup(
    onValidityChange: (Boolean) -> Unit
) {
    var recoveryEmail by rememberSaveable { mutableStateOf("") }
    val focusEmail = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusEmail.requestFocus() }

    LaunchedEffect(recoveryEmail) {
        onValidityChange(recoveryEmail.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(recoveryEmail).matches())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 120.dp, vertical = 60.dp)
    ) {
        // --- FIXED HEADER ---
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

        // --- CENTER CONTENT ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ACCOUNT RECOVERY",
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Palette.DarkBeigeText.copy(alpha = 0.5f),
                    letterSpacing = 1.5.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            StringField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusEmail),
                value = recoveryEmail,
                onValueChange = { recoveryEmail = it },
                header = "Recovery Email",
                placeholder = "Enter an email for password recovery",
                maxLength = 99,
                showCounter = false,
            )
        }
    }
}