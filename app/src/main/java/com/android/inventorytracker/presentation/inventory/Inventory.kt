package com.android.inventorytracker.presentation.inventory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.inventorytracker.presentation.inventory.component.AddNewItemButton
import com.android.inventorytracker.presentation.popup.add_new_item.AddNewItemPopup
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel

@Composable
fun Inventory(
    viewModel: ItemViewModel = viewModel(),
    modifier: Modifier = Modifier) {
    val showAddNewItemPopup = rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        AddNewItemButton(onClick = { showAddNewItemPopup.value = true })
    }
    if (showAddNewItemPopup.value) {
        AddNewItemPopup(showPopup = showAddNewItemPopup, viewModel)
    }
}
