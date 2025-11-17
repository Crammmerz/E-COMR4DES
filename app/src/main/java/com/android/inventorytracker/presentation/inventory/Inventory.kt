package com.android.inventorytracker.presentation.inventory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.inventory.component.*
import com.android.inventorytracker.presentation.popup.item_insertion.InsertItemPopup
import com.android.inventorytracker.presentation.popup.item_removal.DeleteItemPopup
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Inventory(
    itemModels: List<ItemModel>,
    itemViewModel: ItemViewModel,
    batchViewModel: BatchViewModel,
    modifier: Modifier = Modifier
) {
    var showAddItem by rememberSaveable { mutableStateOf(false) }
    var showDeleteItem by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AddNewItemButton(onClick = { showAddItem = true })
            DeleteItemButton(onClick = { showDeleteItem = true})
            Spacer(Modifier.weight(0.5f))
            SearchBar(itemViewModel, Modifier.weight(0.5f).height(45.dp))
            SortDropdownMenu(itemViewModel)
        }

        HeaderSection()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            items(items = itemModels, key = { it.item.id }) { itemModel ->
                ItemDataRow(
                    itemModel = itemModel,
                    itemViewModel = itemViewModel,
                    batchViewModel = batchViewModel,
                )
            }
        }
    }

    if(showAddItem){
        InsertItemPopup(
            onDismiss = { showAddItem = false },
            onInsert = itemViewModel::insertItem
        )
    }
    if(showDeleteItem){
        DeleteItemPopup(
            model = itemModels,
            onDismiss = { showDeleteItem = false },
            onDelete = itemViewModel::deleteItem
        )
    }
}
