package com.android.inventorytracker.presentation.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

// Define Colors Locally if not in theme
val MainBackground = Color(0xFFFFF9F5)
val DarkBrown = Color(0xFF4A3B32)

@Composable
fun Inventory(
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val itemModels by itemViewModel.itemModelList.collectAsState()
    var showAddItem by rememberSaveable { mutableStateOf(false) }
    var showDeleteItem by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackground) // New Cream Background
            .padding(24.dp), // More breathing room like the design
    ) {
        // Top Action Bar
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
        ) {
            if(loginViewModel.userRole == UserRole.ADMIN){
                AddNewItemButton(onClick = { showAddItem = true })
                DeleteItemButton(onClick = { showDeleteItem = true}, enabled = itemModels.isNotEmpty())
            }
            Spacer(Modifier.weight(1f))
            SearchField(Modifier.width(300.dp)) // White search bar
            SortDropdownMenu()
        }

        // Header Labels
        HeaderSection()

        // List Items
        LazyColumn(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
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