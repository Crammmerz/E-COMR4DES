package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddNewItemButton(onClick: () -> Unit) {
    Button(
        onClick = onClick //TODO: Design The Button
    ) {
        Text("Add new Item")
    }
}

@Composable
fun DeleteItemButton(onClick: () -> Unit){
    Button(
        onClick = onClick //TODO: Design The Button
    ) {
        Text("Remove Item")
    }
}

@Composable
fun ItemButton(text: String, modifier: Modifier,onClick: () -> Unit){
    Button(
        modifier = modifier,
        onClick = onClick //TODO: Design The Button
    ) {
        Text(text)
    }
}