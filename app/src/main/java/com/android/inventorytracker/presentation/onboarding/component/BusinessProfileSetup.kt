package com.android.inventorytracker.presentation.onboarding.component

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.onboarding.viewmodel.OnboardingViewModel
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField

@Composable
fun BusinessProfileSetup(
    onValidityChange: (Boolean) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var name by rememberSaveable { mutableStateOf("") }
    var drink by rememberSaveable { mutableStateOf("") }
    var product by rememberSaveable { mutableStateOf("") }
    var supplier by rememberSaveable { mutableStateOf("") }

    val focusName = remember { FocusRequester() }
    val focusDrink = remember { FocusRequester() }
    val focusFirst = remember { FocusRequester() }
    val focusSupplier = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusName.requestFocus()
    }

    LaunchedEffect(name, drink, product, supplier) {
        val allValid = name.isNotEmpty() && drink.isNotEmpty() && product.isNotEmpty() && supplier.isNotEmpty()
        onValidityChange(allValid)
        if(allValid) viewModel.setBusinessProfile(name, drink, product, supplier)
    }

    StringField(
        value = name,
        onValueChange = { name = it },
        header = "Business Name",
        placeholder = "Enter business name",
        modifier = Modifier.focusRequester(focusName),
        onDone = { focusDrink.requestFocus() }
    )
    StringField(
        value = drink,
        onValueChange = { drink = it },
        header = "Favourite Drink",
        placeholder = "Enter favourite drink to make",
        modifier = Modifier.focusRequester(focusDrink),
        onDone = { focusFirst.requestFocus() }
    )
    StringField(
        value = product,
        onValueChange = { product = it },
        header = "First Product",
        placeholder = "Enter the first product sold",
        modifier = Modifier.focusRequester(focusFirst),
        onDone = { focusSupplier.requestFocus() }
    )
    StringField(
        value = supplier,
        onValueChange = { supplier = it },
        header = "First Supplier",
        placeholder = "Enter the first supplier contracted",
        modifier = Modifier.focusRequester(focusSupplier)
    )
}