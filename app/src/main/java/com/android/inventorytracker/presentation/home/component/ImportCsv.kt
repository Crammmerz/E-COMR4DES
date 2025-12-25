package com.android.inventorytracker.presentation.home.component

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.home.viewmodel.HomeViewModel
import com.android.inventorytracker.presentation.popup.csv_removal.CsvRemovalPopup
import com.android.inventorytracker.presentation.popup.csv_removal.viewmodel.CsvViewModel
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans

@Composable
fun ImportCsv(
    doImport: Boolean,
    onDismiss: () -> Unit = {},
    csvViewModel: CsvViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val showDialogPref by homeViewModel.showImportConfirmation.collectAsState(initial = true)
    var skipConfirmation by remember { mutableStateOf(false) }
    var showCsvRemovalPopup by remember { mutableStateOf(false) }

    // Controls the visibility of the internal Confirmation Step
    var showInternalConfirmation by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var csvUri: Uri? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val isCsv = isCsvFile(context, uri) || context.contentResolver.getType(uri) == "text/csv"
            csvUri = if (isCsv) uri else null
        }
    }

    // Logic to handle the sequence of events
    fun initiateFilePicker() {
        if (showDialogPref) {
            showInternalConfirmation = true
        } else {
            launcher.launch("text/*")
            onDismiss()
        }
    }

    LaunchedEffect(csvUri) {
        if (csvUri != null) {
            csvViewModel.loadCsv(csvUri!!)
            showCsvRemovalPopup = true
        }
    }

    if (doImport) {
        // STEP 1: The main Action Popup (Inspired by PhotoSelection/Insertion style)
        if (!showInternalConfirmation) {
            Dialog(onDismissRequest = { onDismiss() }) {
                Card(
                    modifier = Modifier.width(450.dp).wrapContentHeight(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Stock Deduction",
                            style = TextStyle(fontFamily = GoogleSans, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Palette.ButtonDarkBrown)
                        )
                        Text(
                            text = "Choose an import method to deduct inventory",
                            style = TextStyle(fontFamily = GoogleSans, fontSize = 14.sp, color = Color.Gray)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Large Styled Button for CSV Import
                        OutlinedButton(
                            onClick = { initiateFilePicker() },
                            modifier = Modifier.fillMaxWidth().height(80.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White.copy(alpha = 0.5f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = Palette.ButtonDarkBrown,
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    text = "Import CSV File",
                                    style = TextStyle(fontFamily = GoogleSans, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            CancelButton(onClick = onDismiss)
                        }
                    }
                }
            }
        } else {
            // STEP 2: The Confirmation Dialog (Only shows if showDialogPref is true)
            Dialog(onDismissRequest = { showInternalConfirmation = false }) {
                Card(
                    modifier = Modifier.width(420.dp).wrapContentHeight(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Palette.PopupSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Confirm Import",
                            style = TextStyle(fontFamily = GoogleSans, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "You are about to open the file manager to select your inventory data. Continue?",
                            style = TextStyle(fontFamily = GoogleSans, fontSize = 14.sp, color = Color.DarkGray)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = skipConfirmation,
                                onCheckedChange = { skipConfirmation = it },
                                colors = CheckboxDefaults.colors(checkedColor = Palette.ButtonDarkBrown)
                            )
                            Text("Don't ask me again", style = TextStyle(fontFamily = GoogleSans, fontSize = 12.sp, color = Color.Gray))
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.weight(1f))
                            CancelButton(onClick = { showInternalConfirmation = false })
                            ConfirmButton(
                                text = "Continue",
                                containerColor = Palette.ButtonDarkBrown,
                                onClick = {
                                    homeViewModel.toggleImportConfirmation(!skipConfirmation)
                                    launcher.launch("text/*")
                                    onDismiss()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCsvRemovalPopup) {
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