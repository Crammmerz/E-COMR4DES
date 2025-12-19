package com.android.inventorytracker.presentation.home.component

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.home.viewmodel.HomeViewModel
import com.android.inventorytracker.presentation.popup.csv_removal.CsvRemovalPopup
import com.android.inventorytracker.presentation.popup.csv_removal.viewmodel.CsvViewModel

@Composable
fun ImportCsv(
    doImport: Boolean,
    onDismiss: () -> Unit = {},
    csvViewModel: CsvViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
){
    val showDialog by homeViewModel.showImportConfirmation.collectAsState(initial = true)
    var skipConfirmation by remember { mutableStateOf(false) }
    var showCsvRemovalPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var csvUri: Uri? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val isCsv = isCsvFile(context, uri) ||
                    context.contentResolver.getType(uri) == "text/csv"
            csvUri = if (isCsv) uri else null
            Log.d("CsvViewModel", "csvUri: $csvUri")
        }
    }


    LaunchedEffect(csvUri) {
        if(csvUri != null) {
            csvViewModel.loadCsv(csvUri!!)
            showCsvRemovalPopup = true
        }
    }

    if (doImport) {
        when (showDialog) {
            true -> {
                AlertDialog(
                    onDismissRequest = { onDismiss() },
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
                        TextButton(onClick = {
                            homeViewModel.toggleImportConfirmation(!skipConfirmation)
                            launcher.launch("text/*")
                            onDismiss()
                        }) {
                            Text("Yes, Import")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            homeViewModel.toggleImportConfirmation(!skipConfirmation)
                            onDismiss()
                        }) {
                            Text("Cancel")
                        }
                    }
                )
            }
            false -> {
                launcher.launch("text/*")
                onDismiss()
            }
        }
    }

    if(showCsvRemovalPopup){
        CsvRemovalPopup(onDismiss = {
            showCsvRemovalPopup = false
            csvUri = null
        })
    }
}

private fun isCsvFile(context: Context, uri: Uri): Boolean {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst()) {
            val fileName = it.getString(nameIndex)
            return fileName.endsWith(".csv", ignoreCase = true)
        }
    }
    return false
}
