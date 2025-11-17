package com.android.inventorytracker.presentation.popup.item_detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.local.entities.ItemEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.popup.batch_insertion.InsertBatchPopup
import com.android.inventorytracker.presentation.popup.item_detail.component.ItemDetailView
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel

enum class ScreenMode{
    ITEM, ADD_BATCH, DELETE_BATCH
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemDetailPopup(
    itemModel: ItemModel,
    batchViewModel: BatchViewModel,
    onDismiss: () -> Unit,
    onUpdateItem: (ItemEntity) -> Unit,
    onUpdateBatch: (List<ItemBatchEntity>, Int, Int) -> Unit,
) {
    var currentMode by rememberSaveable { mutableStateOf(ScreenMode.ITEM) }

    when(currentMode){
        ScreenMode.ITEM -> {
            ItemDetailView(
                itemModel = itemModel,
                onDismiss = onDismiss,
                onUpdateItem = onUpdateItem,
                onUpdateBatch = onUpdateBatch,
                setMode = {currentMode = it}
            )
        }

        ScreenMode.ADD_BATCH -> {
            LaunchedEffect(currentMode) {
                if (currentMode == ScreenMode.ADD_BATCH) {
                    batchViewModel.unitReset()
                }
            }

            InsertBatchPopup(
                itemModel = itemModel,
                unit = batchViewModel.unit,
                subUnit = batchViewModel.subUnit,
                onUnitChange = { batchViewModel.onUnitChange(it, itemModel.item.subUnitThreshold) },
                onSubUnitChange = { batchViewModel.onSubUnitChange(it, itemModel.item.subUnitThreshold) },
                onDismiss = { currentMode = ScreenMode.ITEM },
                onStore = batchViewModel::storeBatch
            )
        }
        ScreenMode.DELETE_BATCH -> {
            onDismiss()
        }
    }
}
