package com.android.inventorytracker.presentation.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
import com.android.inventorytracker.ui.theme.Palette
import com.android.inventorytracker.ui.theme.GoogleSans

@Composable
fun Inventory(
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val backgroundColor = Color(0xFFFEF7ED)
    val role = loginViewModel.userRole // Kinuha ang role para sa conditional styling

    val itemModels by itemViewModel.itemModelList.collectAsState()
    var showAddItem by rememberSaveable { mutableStateOf(false) }
    var showDeleteItem by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "Inventory",
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 34.sp,
                    color = Palette.DarkBeigeText
                )
            )

            /* 🔹 HEADER ROW (Adaptive Search Bar) */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Admin Buttons: Pakikita lang kung Admin
                if (role == UserRole.ADMIN) {
                    AddNewItemButton { showAddItem = true }
                    DeleteItemButton(
                        onClick = { showDeleteItem = true },
                        enabled = itemModels.isNotEmpty()
                    )

                    // Spacer para itulak ang Search sa kanan kapag Admin
                    Spacer(Modifier.weight(1f))
                }

                // Search Bar: Stretch kapag Staff, Fixed width kapag Admin
                SearchField(
                    modifier = if (role == UserRole.STAFF) {
                        Modifier.weight(1f) // Ito ang mag-stretch sa Search Bar
                    } else {
                        Modifier.width(350.dp) // Fixed width kapag kasama ang Admin buttons
                    }
                )

                SortDropdownMenu()
            }

            // List Content Section
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HeaderSection(modifier = Modifier.fillMaxWidth())

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(itemModels, key = { it.item.id }) { itemModel ->
                        ItemDataRow(
                            model = itemModel,
                            itemViewModel = itemViewModel,
                        )
                    }
                }
            }
        }
    }

    // Popup logic
    if (showAddItem) {
        InsertItemPopup(
            onDismiss = { showAddItem = false },
            onInsert = { itemEntity ->
                itemViewModel.insertItem(itemEntity)
            }
        )
    }

    if (showDeleteItem) {
        DeleteItemPopup(
            model = itemModels,
            onDismiss = { showDeleteItem = false },
            onDelete = { selectedIds ->
                itemModels.filter { it.item.id in selectedIds }.forEach {
                    itemViewModel.deleteItem(it.item)
                }
            }
        )
    }
}