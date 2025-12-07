package com.android.inventorytracker.presentation.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.inventory.component.*
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.popup.item_insertion.InsertItemPopup
import com.android.inventorytracker.presentation.popup.item_removal.DeleteItemPopup
import com.android.inventorytracker.presentation.shared.component.input_fields.SearchBar
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel

@Composable
fun Inventory(
    itemModels: List<ItemModel>,
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var showAddItem by rememberSaveable { mutableStateOf(false) }
    var showDeleteItem by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(loginViewModel.userRole == UserRole.ADMIN){
                AddNewItemButton(onClick = { showAddItem = true })
                DeleteItemButton(onClick = { showDeleteItem = true}, enabled = itemModels.isNotEmpty())
            }
            Spacer(Modifier.weight(1f))
            SearchBar(Modifier.width(275.dp).height(40.dp))
            SortDropdownMenu(itemViewModel)
        }

        HeaderSection()

        LazyColumn(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items = itemModels, key = { it.item.id }) { itemModel ->
                ItemDataRow(
                    model = itemModel,
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
