package com.android.inventorytracker.presentation.inventory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.presentation.inventory.component.AddNewItemButton
import com.android.inventorytracker.presentation.inventory.component.HeaderSection
import com.android.inventorytracker.presentation.inventory.component.ItemOverviewSection
import com.android.inventorytracker.presentation.popup.add_new_item.AddNewItemPopup
import kotlinx.coroutines.flow.Flow


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Inventory(
    itemList: Flow<List<ItemEntity>>,
    itemBatch: Flow<List<ItemBatchEntity>>,
    showAddNewItemPopup: Boolean,
    onShowPopupChange: (Boolean) -> Unit,
    onAddItem: (ItemEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemListState by itemList.collectAsState(initial = emptyList())
    val itemBatchState by itemBatch.collectAsState(initial = emptyList())
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row {
            AddNewItemButton(onClick = { onShowPopupChange(true) })
            Spacer(Modifier.weight(1f))
        }

        HeaderSection()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            items(items = itemListState, key = { it.id }) { item ->
                val matchingBatch = itemBatchState.find { it.itemId == item.id }
                ItemOverviewSection(item, matchingBatch)
            }
        }
    }

    AddNewItemPopup(
        isVisible = showAddNewItemPopup,
        onDismiss = { onShowPopupChange(false) },
        onAdd = { newItem -> onAddItem(newItem) }
    )
}
