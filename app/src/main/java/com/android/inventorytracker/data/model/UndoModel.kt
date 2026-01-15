package com.android.inventorytracker.data.model

import com.android.inventorytracker.data.local.entities.ItemBatchEntity

enum class BatchOperation {
    ADD, DEDUCT
}

data class UndoModel(
    val batches: List<ItemBatchEntity>,
    val operation: BatchOperation,
)