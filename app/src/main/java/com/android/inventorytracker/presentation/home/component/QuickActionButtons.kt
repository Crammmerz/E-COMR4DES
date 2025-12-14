package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.home.viewmodel.HomeViewModel
import com.android.inventorytracker.presentation.popup.batch_group_insertion.BatchGroupInsertionPopup
import com.android.inventorytracker.presentation.popup.batch_group_removal.BatchGroupRemovalPopup
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun QuickActions(
    modifier: Modifier = Modifier,
    itemViewModel: ItemViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val model by itemViewModel.itemModelList.collectAsState(initial = emptyList())

    var showAddStock by rememberSaveable { mutableStateOf(false) }
    var showRemoveStock by rememberSaveable { mutableStateOf(false) }
    var showAddCSV by rememberSaveable { mutableStateOf(false) }
    var showRemoveCSV by rememberSaveable { mutableStateOf(false) }

    val showCsvConfirmation by homeViewModel.showImportConfirmation.collectAsState(initial = true)

    Row(
        modifier = modifier
            .height(60.dp)
            .padding(vertical = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.Start)
    ) {
        QuickActionButton(
            label = "Add Stock",
            modifier = Modifier.weight(1f),
            bgColor = Palette.AccentBeigePrimary,
            contentColor = Palette.PureWhite,
            onClick = { showAddStock = true }
        )

        QuickActionButton(
            label = "Deduct Stock",
            modifier = Modifier.weight(1f),
            bgColor = Palette.AccentBeigeLight,
            contentColor = Palette.DarkBeigeText,
            onClick = { showRemoveStock = true }
        )

        QuickActionButton(
            label = "Add Stocks (.csv)",
            modifier = Modifier.weight(1f),
            bgColor = Palette.PureWhite,
            contentColor = Palette.DarkBeigeText,
            onClick = { showAddCSV = true }
        )

        QuickActionButton(
            label = "Deduct Stocks (.csv)",
            modifier = Modifier.weight(1f),
            bgColor = Palette.PureWhite,
            contentColor = Palette.DarkBeigeText,
            onClick = { showRemoveCSV = true }
        )
    }

    if (showAddStock) {
        BatchGroupInsertionPopup(model = model, onDismiss = { showAddStock = false })
    }
    if (showRemoveStock) {
        BatchGroupRemovalPopup(model = model, onDismiss = { showRemoveStock = false })
    }

    if (showAddCSV && showCsvConfirmation) {
        var skipConfirmation by rememberSaveable { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddCSV = false },
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
                TextButton(onClick = {
                    homeViewModel.toggleImportConfirmation(!skipConfirmation)
                    showAddCSV = false
                }) { Text("Yes, Import") }
            },
            dismissButton = {
                TextButton(onClick = { showAddCSV = false }) { Text("Cancel") }
            }
        )
    }

    if (showRemoveCSV && showCsvConfirmation) {
        var skipConfirmation by rememberSaveable { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showRemoveCSV = false },
            title = { Text("Import CSV File (Remove Stock)") },
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
                    showRemoveCSV = false
                }) { Text("Yes, Import") }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveCSV = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun QuickActionButton(
    label: String,
    modifier: Modifier = Modifier,
    bgColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(
                color = bgColor,
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = label,
            color = contentColor,
        )
    }
}