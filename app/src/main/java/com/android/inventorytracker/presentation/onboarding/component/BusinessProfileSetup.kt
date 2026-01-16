package com.android.inventorytracker.presentation.onboarding.component

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.onboarding.viewmodel.OnboardingViewModel
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun BusinessProfileSetup(
    onValidityChange: (Boolean) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var name by rememberSaveable { mutableStateOf("") }

    var triedNext by rememberSaveable { mutableStateOf(false) }

    val focusName = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusName.requestFocus()
    }

    val isValid = name.isNotBlank()

    LaunchedEffect(isValid) {
        onValidityChange(isValid)
        if (isValid) {
            viewModel.setBusinessProfile(name)
        }
    }

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

        // CONTENT
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "BUSINESS PROFILE",
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Palette.DarkBeigeText.copy(alpha = 0.5f),
                    letterSpacing = 1.5.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // BUSINESS NAME
                StringField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusName),
                    value = name,
                    onValueChange = { name = it },
                    header = "Business Name",
                    placeholder = "What is the name of your business?",
                    maxLength = 99,
                    showCounter = false,
                )

                if (triedNext && name.isBlank()) {
                    Text(
                        text = "Business name cannot be empty",
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontFamily = GoogleSans
                    )
                }
            }
        }
    }
}
