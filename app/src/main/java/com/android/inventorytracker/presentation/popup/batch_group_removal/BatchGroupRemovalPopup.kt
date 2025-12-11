package com.android.inventorytracker.presentation.popup.batch_group_removal

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.RemoveBatch
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.batch_group_removal.component.ItemRemovalRow
import com.android.inventorytracker.presentation.shared.component.SortDropdownMenu
import com.android.inventorytracker.presentation.shared.component.input_fields.SearchField
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel

@Composable
fun BatchGroupRemovalPopup(
    model: List<ItemModel>,
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val persistentItems by itemViewModel.persistentItems.collectAsState()
    var inputMap by remember { mutableStateOf<Map<Int, RemoveBatch>>(emptyMap()) }
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
            Text("Quick Remove")
            Row (horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.End)) {
                SearchField(Modifier.weight(1f))
                SortDropdownMenu()
            }
            Text("Select Items and Set Values (First Expiry, First Out (FEFO))", style = MaterialTheme.typography.bodySmall,)
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
                    ItemRemovalRow (
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
                    }
                }) {
                    Text("Remove Batch")
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
                    Text("Do you want to remove these batches?")
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 300.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(validPersistentItems) { remove ->
                            Text("• ${remove.itemName} (remove unit: ${remove.unit} subunit: ${remove.subunit})", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            },
            onDismissRequest = { showConfirmation = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        validPersistentItems.forEach { operation ->
                            batchViewModel.onDeductStock(operation.batches, operation.subunit)
                        }
                        itemViewModel.reset()
                        onDismiss()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}