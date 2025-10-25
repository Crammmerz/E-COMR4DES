package com.android.inventorytracker.presentation.popup.add_new_item.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CancelButton(onClick:  () -> Unit){
    Button(onClick = onClick) {
        Text("Cancel")
    }
}