package com.android.inventorytracker.presentation.onboarding.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.onboarding.viewmodel.OnboardingViewModel
import com.android.inventorytracker.presentation.shared.component.input_fields.PinField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun AccRecoverySetup(
    onValidityChange: (Boolean) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
){
    var pin by rememberSaveable { mutableStateOf("") }
    var phrase by rememberSaveable { mutableStateOf("") }

    var validPin by rememberSaveable { mutableStateOf(false) }
    var validPhrase by rememberSaveable { mutableStateOf(false) }

    val focusPin = remember { FocusRequester() }
    val focusPhrase = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusPin.requestFocus() }
    LaunchedEffect(pin, phrase) {
        val valid = validPin && validPhrase && pin.isNotEmpty() && phrase.isNotEmpty()
        onValidityChange(valid)
        if(valid) {
            viewModel.setSecurityRecovery(pin, phrase)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 120.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // --- HEADER ---
        Text(
            text = "Inventory Tracker",
            style = TextStyle(
                fontFamily = GoogleSans,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Palette.DarkBeigeText,
                letterSpacing = (-2.5).sp
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "ACCOUNT RECOVERY SETUP",
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Palette.DarkBeigeText.copy(alpha = 0.5f),
                letterSpacing = 1.5.sp
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- INPUT FIELDS (Full Width / Stretched) ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            PinField(
                modifier = Modifier
                    .fillMaxWidth() // Stretched to full width
                    .focusRequester(focusPin),
                fieldModifier = Modifier.focusRequester(focusPin),
                value = pin,
                onValueChange = { pin = it },
                header = "Recovery PIN",
                onDone = { focusPhrase.requestFocus() },
                onValidityChange = { validPin = it },
            )

            StringField(
                modifier = Modifier
                    .fillMaxWidth() // Stretched to full width
                    .focusRequester(focusPhrase),
                value = phrase,
                onValueChange = { phrase = it },
                header = "Recovery Phrase ",
                placeholder = "Enter a phrase that will help you recover your account ",
                onValidityChange = { validPhrase = it }
            )
        }
    }
}