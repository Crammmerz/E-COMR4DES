package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AddNewItemButton(onClick: () -> Unit) {
    Button(
        onClick = onClick //TODO: Design The Button
    ) {
        Text("Add new Item")
    }
}
