package com.android.inventorytracker.presentation.popup.add_new_batch.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AddBatchButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Add Stock")
    }
}

@Composable
fun CancelButton(onClick: () -> Unit){
    Button(onClick = onClick) {
        Text("Cancel")
    }
}
