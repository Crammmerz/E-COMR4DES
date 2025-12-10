package com.android.inventorytracker.presentation.popup.batch_group_insertion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.batch_group_insertion.component.ItemRow
import com.android.inventorytracker.presentation.shared.component.SortDropdownMenu
import com.android.inventorytracker.presentation.shared.component.input_fields.SearchField
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.android.inventorytracker.data.model.SortBy
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel

@Composable
fun BatchGroupInsertionPopup(
    model: List<ItemModel>,
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    var doStoreBatch by rememberSaveable { mutableStateOf(false) }
    val persistentItems by itemViewModel.persistentItems.collectAsState()
    var validityMap by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }

    DialogHost(
        modifier = Modifier
            .size(600.dp),
        onDismissRequest = {
            itemViewModel.setSearchQuery("")
            itemViewModel.resetPersistence()
            itemViewModel.setSort(SortBy.NAME_ASC)
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
                items(model) { model ->
                    val persistence = model.item.id in persistentItems
                    ItemRow(
                        model = model,
                        isPersistent = persistence,
                        doStoreBatch = doStoreBatch,
                        onStoreBatch = {
                            if (persistence && doStoreBatch) {
                                batchViewModel.onStoreBatch(it)
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
                    itemViewModel.setSearchQuery("")
                    itemViewModel.resetPersistence()
                    itemViewModel.setSort(SortBy.NAME_ASC)
                    onDismiss()
                }) {
                    Text("Close")
                }
                Button(onClick = {
                    val validPersistentItems = model.filter { m ->
                        m.item.id in persistentItems && (validityMap[m.item.id] == true)
                    }
                    if (validPersistentItems.isNotEmpty()) {
                        doStoreBatch = true
                        onDismiss()
                    }
                }) {
                    Text("Store Batch")
                }
            }
        }
    }
}