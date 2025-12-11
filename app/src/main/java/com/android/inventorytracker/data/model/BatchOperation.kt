package com.android.inventorytracker.data.model

import com.android.inventorytracker.data.local.entities.ItemBatchEntity

data class RemoveBatch(
    val itemId: Int,
    val itemName: String,
    val batches: List<ItemBatchEntity>,
    val unit: Float,
    val subunit: Int,
)

data class InsertBatch(
    val itemId: Int,
    val itemName: String,
    val unit: Float,
    val subunit: Int,
    val expiryDate: Long,
)
