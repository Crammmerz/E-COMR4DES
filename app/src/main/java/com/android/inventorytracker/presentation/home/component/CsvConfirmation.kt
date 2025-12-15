package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.home.viewmodel.HomeViewModel

@Composable
fun AddCsvConfirmation(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var skipConfirmation by remember { mutableStateOf(false) }

    fun dismiss(){
        homeViewModel.toggleImportConfirmation(!skipConfirmation)
        onDismiss()
    }

    AlertDialog(
        onDismissRequest = { dismiss() },
        title = { Text("Import CSV File (Add Stock)") },
        text = {
            Column {
                Text("Do you want to import data from this CSV file?")
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = skipConfirmation,
                        onCheckedChange = { skipConfirmation = it }
                    )
                    Text("Don't ask me again")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    homeViewModel.toggleImportConfirmation(!skipConfirmation)
                    onConfirm()
                    dismiss()
                }) {
                Text("Yes, Import")
            }
        },
        dismissButton = {
            TextButton(onClick = { dismiss() }) {
                homeViewModel.toggleImportConfirmation(!skipConfirmation)
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DeductCsvConfirmation(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
){
    var skipConfirmation by remember { mutableStateOf(false) }

    fun dismiss(){
        homeViewModel.toggleImportConfirmation(!skipConfirmation)
        onDismiss()
    }

    AlertDialog(
        onDismissRequest = { dismiss() },
        title = { Text("Import CSV File (Deduct Stock)") },
        text = {
            Column {
                Text("Would you like to open the file manager to select a CSV file for import?")
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = skipConfirmation,
                        onCheckedChange = { skipConfirmation = it }
                    )
                    Text("Don't ask me again")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    dismiss()
                }) { Text("Yes, Import") }
        },
        dismissButton = {
            TextButton(onClick = { dismiss() }) {
                Text("Cancel")
            }
        }
    )
}