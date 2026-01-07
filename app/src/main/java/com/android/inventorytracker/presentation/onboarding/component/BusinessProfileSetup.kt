package com.android.inventorytracker.presentation.onboarding.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
    // Kinatize na ang state: Business Name at First Product na lang
    var name by rememberSaveable { mutableStateOf("") }
    var product by rememberSaveable { mutableStateOf("") }

    val focusName = remember { FocusRequester() }
    val focusProduct = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusName.requestFocus() }

    // Validity logic updated para sa dalawang fields na lang
    LaunchedEffect(name, product) {
        val allValid = name.isNotEmpty() && product.isNotEmpty()
        onValidityChange(allValid)
        if(allValid) {
            // Note: I-adjust ang ViewModel function kung kailangan
            // pero pinapasa natin ang empty strings sa tinanggal na fields para walang error
            viewModel.setBusinessProfile(name, "", product, "")
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

        // --- INPUT FIELDS (Full Width / Stretched) ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            StringField(
                modifier = Modifier
                    .fillMaxWidth() // Stretched to full width
                    .focusRequester(focusName),
                value = name,
                onValueChange = { name = it },
                header = "Business Name", // Pinalaki ang internal font sa StringField component mo dapat
                placeholder = "What is the name of your business?",
                onDone = { focusProduct.requestFocus() }
            )

            StringField(
                modifier = Modifier
                    .fillMaxWidth() // Stretched to full width
                    .focusRequester(focusProduct),
                value = product,
                onValueChange = { product = it },
                header = "First Product Sold",
                placeholder = "What was the first product you sold?",
                onDone = { /* Action after last field */ }
            )
        }
    }
}