package com.android.inventorytracker.presentation.main.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.inventorytracker.data.local.database.InventoryDatabase
import com.android.inventorytracker.data.repository.ItemRepository
import com.android.inventorytracker.presentation.home.Home
import com.android.inventorytracker.presentation.inventory.Inventory
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.presentation.main.viewmodel.Content
import com.android.inventorytracker.presentation.main.viewmodel.ContentViewModel
import com.android.inventorytracker.ui.theme.Sand
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentSection(
    modifier: Modifier = Modifier,
    bgColor: Color = Sand,
    contentViewModel: ContentViewModel = viewModel()
) {
    val context = LocalContext.current
    val db = InventoryDatabase.getDatabase(context)
    val itemViewModel = ItemViewModel(
        ItemRepository(db.itemDao(), db.itemBatchDao())
    )
    val coroutineScope = rememberCoroutineScope()

    val itemList = itemViewModel.itemList
    val itemBatchList = itemViewModel.itemBatchList
    var showPopup by remember { mutableStateOf(false) }

    Surface(
        color = bgColor,
        tonalElevation = 10.dp,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        when (contentViewModel.currentContent) {
            Content.Home -> Home()
            Content.Inventory -> Inventory(
                itemList,
                itemBatchList,
                showPopup,
                {showPopup = it},
                onAddItem = { item ->
                    coroutineScope.launch {
                        itemViewModel.insertItem(item)
                    }
                }
            )
        }
    }
}
