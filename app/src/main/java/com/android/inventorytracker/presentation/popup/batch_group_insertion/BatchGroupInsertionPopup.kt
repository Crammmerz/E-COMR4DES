package com.android.inventorytracker.presentation.popup.batch_group_insertion

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.batch_group_insertion.component.ItemInsertionRow
import com.android.inventorytracker.presentation.shared.component.SortDropdownMenu
import com.android.inventorytracker.presentation.shared.component.input_fields.SearchField
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.model.InsertBatch
import com.android.inventorytracker.data.model.RemoveBatch
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.util.toFormattedDateString
import com.android.inventorytracker.util.toLocalDate

@Composable
fun BatchGroupInsertionPopup(
    model: List<ItemModel>,
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val persistentItems by itemViewModel.persistentItems.collectAsState()
    var inputMap by remember { mutableStateOf<Map<Int, InsertBatch>>(emptyMap()) }
    var validityMap by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }
    var showConfirmation by remember { mutableStateOf(false) }

    DialogHost(
        modifier = Modifier
            .size(600.dp),
        onDismissRequest = {
            itemViewModel.reset()
            onDismiss()
        },
        useImePadding = true
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Quick Add")
            Row (horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.End)) {
                SearchField(Modifier.weight(1f))
                SortDropdownMenu()
            }
            Text("Select Items and Set Values", style = MaterialTheme.typography.bodySmall,)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(model, key = { it.item.id }) { model ->
                    val persistence = model.item.id in persistentItems
                    ItemInsertionRow(
                        model = model,
                        isPersistent = persistence,
                        onValueChange = { operation ->
                            if (persistence) {
                                inputMap = inputMap + (model.item.id to operation)
                            }
                        },
                        onValidityChange = { isValid ->
                            if (persistence) {
                                validityMap = validityMap + (model.item.id to isValid)
                            }
                        }
                    )
                }
            }
            Row {
                Button(onClick = {
                    itemViewModel.reset()
                    onDismiss()
                }) {
                    Text("Close")
                }
                Button(onClick = {
                    val validPersistentItems = model.filter { m ->
                        m.item.id in persistentItems && (validityMap[m.item.id] == true)
                    }
                    if (validPersistentItems.isNotEmpty()) {
                        showConfirmation = true
                    } else {
                        Toast.makeText(
                            context,
                            "Oops! Looks like something’s missing — select an item or fill in the required fields.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    Text("Store Batch")
                }
            }
        }
    }

    if (showConfirmation) {
        val validPersistentItems = inputMap.filter { (id, _) ->
            id in persistentItems && (validityMap[id] == true)
        }.values.toList()
        AlertDialog(
            title = { Text("Confirm Action") },
            text = {
                Column (verticalArrangement = Arrangement.spacedBy(4.dp) ){
                    Text("Do you want to add these batches?")
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 300.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(validPersistentItems) { insert ->
                            Text(
                                text = "• ${insert.itemName} " +
                                        "(insert unit: ${insert.unit}, " +
                                        "subunit: ${insert.subunit}, " +
                                        "expiry: ${insert.expiryDate.toFormattedDateString()})",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            },
            onDismissRequest = { showConfirmation = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        validPersistentItems.forEach { operation ->
                            batchViewModel.onStoreBatch(
                                batch = ItemBatchEntity(
                                    itemId = operation.itemId,
                                    subUnit = operation.subunit,
                                    expiryDate = operation.expiryDate
                                )
                            )
                        }
                        itemViewModel.reset()
                        onDismiss()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmation = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}