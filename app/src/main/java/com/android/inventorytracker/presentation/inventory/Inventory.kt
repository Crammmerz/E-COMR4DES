package com.android.inventorytracker.presentation.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.model.UserRole
import com.android.inventorytracker.presentation.inventory.component.*
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.popup.item_insertion.InsertItemPopup
import com.android.inventorytracker.presentation.popup.item_removal.DeleteItemPopup
import com.android.inventorytracker.presentation.shared.component.SortDropdownMenu
import com.android.inventorytracker.presentation.shared.component.input_fields.SearchField
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel

@Composable
fun Inventory(
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val BackgroundColor = Color(0xFFFEF7ED)

    val itemModels by itemViewModel.itemModelList.collectAsState()
    var showAddItem by rememberSaveable { mutableStateOf(false) }
    var showDeleteItem by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(vertical = 8.dp)
    ) {

        /* 🔹 HEADER ROW (ALIGNED WITH LIST ITEMS) */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // ✅ MATCH ITEM ROW PADDING
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (loginViewModel.userRole == UserRole.ADMIN) {
                AddNewItemButton { showAddItem = true }
                DeleteItemButton(
                    onClick = { showDeleteItem = true },
                    enabled = itemModels.isNotEmpty()
                )
            }

            Spacer(Modifier.weight(1f))

            SearchField(Modifier.width(260.dp))
            SortDropdownMenu()
        }

        Spacer(modifier = Modifier.height(8.dp))

        HeaderSection(
            modifier = Modifier.padding(horizontal = 16.dp) // ✅ align headers too
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), // ✅ SAME AS HEADER
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(itemModels, key = { it.item.id }) {
                ItemDataRow(
                    model = it,
                    itemViewModel = itemViewModel,
                    batchViewModel = batchViewModel
                )
            }
        }
    }

    if (showAddItem) {
        InsertItemPopup(
            onDismiss = { showAddItem = false },
            onInsert = itemViewModel::insertItem
        )
    }

    if (showDeleteItem) {
        DeleteItemPopup(
            model = itemModels,
            onDismiss = { showDeleteItem = false },
            onDelete = itemViewModel::deleteItem
        )
    }
}
